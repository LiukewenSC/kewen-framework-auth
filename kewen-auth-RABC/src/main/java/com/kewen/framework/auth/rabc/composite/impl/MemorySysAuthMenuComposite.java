package com.kewen.framework.auth.rabc.composite.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.model.MenuTypeConstant;
import com.kewen.framework.auth.rabc.model.req.MenuSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuResp;
import com.kewen.framework.auth.rabc.model.resp.MenuRespBase;
import com.kewen.framework.auth.rabc.mp.entity.SysAuthMenu;
import com.kewen.framework.auth.rabc.mp.entity.SysMenu;
import com.kewen.framework.auth.rabc.mp.service.SysAuthMenuMpService;
import com.kewen.framework.auth.rabc.mp.service.SysMenuMpService;
import com.kewen.framework.auth.rabc.utils.BeanUtil;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @descrpition 基于菜单存于内存的实现
 * @author kewen
 * @since 2022-12-01 10:21
 */
@Slf4j
public class MemorySysAuthMenuComposite implements SysAuthMenuComposite {
    @Autowired
    private SysMenuMpService sysMenuService;
    @Autowired
    private SysAuthMenuMpService menuAuthService;

    @Value("${kewen-framework.environment.prod}")
    private boolean isCache = false;

    /**
     * 菜单、权限的缓存
     */
    Cache<Object, Object> cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();




    @Override
    public boolean hasMenuAuth(Collection<BaseAuth> authorities, String url) {
        //SysMenu sysMenu = menuService.getMenuByUrl(url);
        Optional<SysMenu> sysMenuOptional = getSysMenus().stream().filter(m -> Objects.equals(m.getPath(),url)).findFirst();
        if (!sysMenuOptional.isPresent()){
            return false;
        }
        SysMenu sysMenu = sysMenuOptional.get();
        return hasMenuAuth(authorities,sysMenu);
    }

    @Override
    public List<MenuAuthResp> getMenuTree() {
        List<SysMenu> sysMenus = getSysMenus();
        Map<Long, List<SysAuthMenu>> authByMenuMap = getSysMenuAuths().stream()
                .collect(Collectors.groupingBy(SysAuthMenu::getMenuId));
        List<MenuAuthResp> collect = sysMenus.stream()
                .map(l -> BeanUtil.toBean(l, MenuAuthResp.class))
                .peek(m-> {
                    List<SysAuthMenu> SysAuthMenus = authByMenuMap.get(m.getId());
                    if (SysAuthMenus!=null){
                        List<BaseAuth> authList = SysAuthMenus.stream()
                                .map(a -> new BaseAuth(a.getAuthority(), a.getDescription()))
                                .collect(Collectors.toList());
                        SimpleAuthObject authObject = new SimpleAuthObject();
                        authObject.setProperties(authList);
                        m.setAuthObject(authObject);
                    }
                })
                .collect(Collectors.toList());
        return TreeUtil.transfer(collect, 0L);
    }

    @Override
    public List<MenuResp> getCurrentUserMenuTree(Collection<BaseAuth> authorities) {
        //得到全部树
        List<MenuAuthResp> trees = getMenuTree();
        //克隆副本
        List<MenuResp> menuTree = convertMenuAuthResp2MenuResp(trees);
        //将不属于自己的移除
        Iterator<MenuResp> iterator = menuTree.iterator();
        while (iterator.hasNext()) {
            MenuResp next = iterator.next();
            boolean needRemove = needRemove(next, authorities);
            if (needRemove){
                iterator.remove();
            }
        }
        return menuTree;
    }
    private List<MenuResp> convertMenuAuthResp2MenuResp(List<MenuAuthResp> menuAuthResps){
        ArrayList<MenuResp> list = new ArrayList<>();
        for (MenuAuthResp menuAuthResp : menuAuthResps) {
            MenuResp menuResp = new MenuResp();
            BeanUtil.copy(menuAuthResp,menuResp);
            if (!CollectionUtils.isEmpty(menuResp.getChildren())){
                menuResp.setChildren(convertMenuAuthResp2MenuResp(menuAuthResp.getChildren()));
            }
            list.add(menuResp);
        }
        return list;
    }



    @Override
    public void editMenuAuthorities(Long menuId, Collection<BaseAuth> baseAuths) {
        //移除原有的
        menuAuthService.remove(
                new LambdaQueryWrapper<SysAuthMenu>().eq(SysAuthMenu::getMenuId,menuId)
        );
        //批量插入新的
        if (!CollectionUtils.isEmpty(baseAuths)){
            menuAuthService.saveBatch(
                    baseAuths.stream()
                            .map(a->
                                    new SysAuthMenu()
                                            .setMenuId(menuId)
                                            .setAuthority(a.getAuth())
                                            .setDescription(a.getDescription())
                            ).collect(Collectors.toList())
            );
        }
    }


    @Override
    @Transactional
    public void addMenu(MenuSaveReq req) {
        sysMenuService.save(req);
        Long menuId = req.getId();
        editMenuAuthorities(menuId,req.getAuthObject().listBaseAuth());
    }

    @Override
    @Transactional
    public void updateMenu(MenuSaveReq req) {
        sysMenuService.updateById(req);
        Long menuId = req.getId();
        editMenuAuthorities(menuId,req.getAuthObject().listBaseAuth());
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) {
        List<MenuAuthResp> menuTrees = getMenuTree();
        MenuAuthResp menuResp = TreeUtil.fetchSubTree(menuTrees,id);
        if (menuResp==null){
            throw new RuntimeException("菜单为空");
        }
        //获取平菜单列表
        List<MenuAuthResp> deleteMenus = TreeUtil.unTransfer(menuResp);
        //找到需要删除的菜单id
        List<Long> menuIds = deleteMenus.stream().map(MenuRespBase::getId).collect(Collectors.toList());

        //移除菜单
        sysMenuService.removeBatchByIds(menuIds);
        //移除菜单权限
        menuAuthService.remove(
                new LambdaQueryWrapper<SysAuthMenu>()
                        .in(SysAuthMenu::getMenuId,menuIds)
        );
    }


    /**
     * 是否需要移除本菜单，并在内部递归移除掉不属于自己的
     * @param menuResp 当前菜单
     * @param authorities 用户权限
     * @return
     */
    private boolean needRemove(MenuResp menuResp,Collection<BaseAuth> authorities){
        List<MenuResp> subs = menuResp.getChildren();

        if (CollectionUtils.isEmpty(subs)){
            //已经到了最底层，判断自己是否需要移除
            boolean hasAuth = hasMenuAuth(authorities, BeanUtil.toBean(menuResp, SysMenu.class));
            //无权限则移除
            return !hasAuth;
        } else {
            //有子菜单，继续遍历子菜单
            Iterator<MenuResp> iterator = subs.iterator();
            while (iterator.hasNext()) {
                MenuResp next = iterator.next();
                boolean needRemove = needRemove(next,authorities);
                if (needRemove){
                    iterator.remove();
                }
            }
            //子菜单已经移除完了，则需要校验自己是否需要移除
            if (CollectionUtils.isEmpty(subs)){
                boolean hasAuth = hasMenuAuth(authorities, BeanUtil.toBean(menuResp, SysMenu.class));
                //无权限则移除
                return !hasAuth;
            } else {
                //子菜单未移除完，那么子菜单需要展示，自己也一定需要展示
                return false;
            }
        }
    }

    /**
     * 校验菜单权限 ，可递归校验，直到追踪到树根
     * @param authorities
     * @param sysMenu
     * @return
     */
    private boolean hasMenuAuth(Collection<BaseAuth> authorities, SysMenu sysMenu){
        //基于自己的权限
        if (MenuTypeConstant.AUTH_TYPE.OWNER.equals(sysMenu.getAuthType())){
            Long menuId = sysMenu.getId();
            return getSysMenuAuths().stream()
                    .filter(a -> a.getMenuId().equals(menuId))
                    .map(a->new BaseAuth(a.getAuthority(),a.getDescription()))
                    .anyMatch(authorities::contains);
        } else if (MenuTypeConstant.AUTH_TYPE.PARENT.equals(sysMenu.getAuthType())){
            //基于父菜单的权限
            Long parentId = sysMenu.getParentId();
            Optional<SysMenu> first = getSysMenus().stream().filter(m -> m.getId().equals(parentId)).findFirst();
            if (!first.isPresent()){
                //已经到顶了，找不到 top对应的parent的菜单
                log.info("上层追溯到根菜单，仍未找到对应的权限");
                return false;
            }
            return hasMenuAuth(authorities,first.get());
        } else {
            throw new RuntimeException("菜单权限类型错误: "+sysMenu.getAuthType());
        }
    }

    /**
     * 获取菜单列表
     * @return
     */
    private List<SysMenu> getSysMenus(){
        if (!isCache){
            return sysMenuService.list();
        }
        try {
            return (List<SysMenu>)cache.get("menus", new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return sysMenuService.list();
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取菜单权限
     * @return
     */
    private List<SysAuthMenu> getSysMenuAuths(){
        if (!isCache){
            return menuAuthService.list();
        }
        try {
            return (List<SysAuthMenu>)cache.get("menuAuths", new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return menuAuthService.list();
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
