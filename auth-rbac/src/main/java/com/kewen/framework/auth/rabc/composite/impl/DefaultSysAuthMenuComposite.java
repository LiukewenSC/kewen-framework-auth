package com.kewen.framework.auth.rabc.composite.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kewen.framework.auth.core.entity.BaseAuth;
import com.kewen.framework.auth.rabc.composite.AuthMenuStore;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.model.MenuTypeConstant;
import com.kewen.framework.auth.rabc.model.req.MenuApiSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuApiAndAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuApiResp;
import com.kewen.framework.auth.rabc.model.resp.MenuRouteResp;
import com.kewen.framework.auth.rabc.mp.entity.SysAuthMenu;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuApi;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRoute;
import com.kewen.framework.auth.rabc.mp.service.SysAuthMenuMpService;
import com.kewen.framework.auth.rabc.mp.service.SysMenuApiMpService;
import com.kewen.framework.auth.rabc.mp.service.SysMenuRouteMpService;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @descrpition 菜单实现
 * @author kewen
 * @since 2022-12-01 10:21
 */
@Service
@Slf4j
public class DefaultSysAuthMenuComposite implements SysAuthMenuComposite {
    @Autowired
    private SysMenuApiMpService sysMenuPathMpService;
    @Autowired
    private SysMenuRouteMpService sysMenuRouteMpService;
    @Autowired
    private SysAuthMenuMpService menuAuthService;

    @Value("${kewen-framework.auth.cache-auth:false}")
    private boolean isCache = false;

    @Autowired
    private AuthMenuStore authMenuStore;

    @Override
    public boolean hasMenuAuth(Collection<BaseAuth> authorities, String requestPath) {
        Optional<SysMenuApi> sysMenuOptional = getApiMenus().stream().filter(m -> Objects.equals(m.getPath(), requestPath)).findFirst();
        if (!sysMenuOptional.isPresent()){
            return false;
        }
        SysMenuApi sysMenuRequest = sysMenuOptional.get();
        return hasMenuAuth(authorities,sysMenuRequest);
    }

    @Override
    public List<MenuApiAndAuthResp> getApiAuthMenuTree(boolean showDeleted) {
        List<SysMenuApi> sysMenus = getApiMenus();

        Map<Long, List<SysAuthMenu>> authByMenuMap = getSysMenuAuths().stream()
                .collect(Collectors.groupingBy(SysAuthMenu::getApiId));
        List<MenuApiAndAuthResp> collect = sysMenus.stream()
                .map(MenuApiAndAuthResp::of)
                //是否过滤已删除的API菜单
                .filter(m-> showDeleted || !m.getDeleted())
                .peek(m-> {
                    List<SysAuthMenu> SysAuthMenus = authByMenuMap.get(m.getId());
                    if (SysAuthMenus != null){
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

    /**
     * 获取权限集的请求菜单树
     * 已删除的API菜单不包含
     */
    @Override
    public List<MenuApiAndAuthResp> getApiAuthMenuTree(Collection<BaseAuth> authorities) {
        //得到全部树
        List<MenuApiAndAuthResp> trees = getApiAuthMenuTree(false);
        //移除掉不是自己的
        TreeUtil.removeIfUnmatch(trees,t ->{
            boolean hasMenuAuth = hasMenuAuth(authorities, t);
            return  !hasMenuAuth;
        });
        return trees;
    }

    @Override
    public List<MenuRouteResp> getRouteAuthMenuTree(boolean showDeleted) {

        List<SysMenuRoute> menuRoutes = getMenuRoutes();
        List<MenuRouteResp> routeResps = menuRoutes.stream()
                .filter(m-> showDeleted || !m.getDeleted())
                .map(MenuRouteResp::from)
                .collect(Collectors.toList());
        return TreeUtil.transfer(routeResps, 0L);
    }

    /**
     * 获取权限集对应的有权限的路由
     * @return
     */
    @Override
    public List<MenuRouteResp> getRouteAuthMenuTree(Collection<BaseAuth> authorities) {
        List<MenuApiAndAuthResp> trees = getApiAuthMenuTree(authorities);
        //克隆副本并转换成 MenuRequestResp
        List<MenuApiResp> requestRespTrees = TreeUtil.convertList(trees, MenuApiResp.class);

        //获取路由树
        List<MenuRouteResp> routeRespTree = getRouteAuthMenuTree(false);
        //移除无用的节点
        //1. 判断MenuRoute对应的请求id是否在拥有的请求树中
        Set<Long> requestIds = requestRespTrees.stream()
                .map(TreeUtil::fetchSubIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        //2. 移除掉无权限的节点  请求地址id为空或者有用的请求id集合包含了当前请求id则都判断为匹配上了，可以展示到前端
        TreeUtil.removeIfUnmatch(routeRespTree,(t)-> !(t.getApiId()==null || requestIds.contains(t.getApiId())));
        return routeRespTree;
    }


    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void editMenuAuthorities(Long apiId, Collection<BaseAuth> baseAuths) {

        List<SysAuthMenu> authMenus = menuAuthService.list(new LambdaQueryWrapper<SysAuthMenu>().eq(SysAuthMenu::getApiId, apiId));

        //需要移除的
        List<SysAuthMenu> removes = authMenus.stream().filter(
                db -> baseAuths.stream().noneMatch(b -> b.getAuth().equals(db.getAuthority()))
        ).collect(Collectors.toList());
        //需要添加的
        List<SysAuthMenu> adds = baseAuths.stream().filter(
                b -> authMenus.stream().noneMatch(db -> db.getAuthority().equals(b.getAuth()))
        ).map(
                b -> new SysAuthMenu().setApiId(apiId).setAuthority(b.getAuth()).setDescription(b.getDescription())
        ).collect(Collectors.toList());

        //移除原有的
        if (CollectionUtil.isNotEmpty(removes)){
            menuAuthService.removeBatchByIds(removes);
        }
        //批量插入新的
        if (CollectionUtil.isNotEmpty(adds)){
            menuAuthService.saveBatch(adds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuApiSaveReq req) {
        sysMenuPathMpService.updateById(req);
        Long apiId = req.getId();
        editMenuAuthorities(apiId,req.getAuthObject().listBaseAuth());
    }

    /**
     * 校验菜单权限 ，可递归校验，直到追踪到树根
     * @param authorities
     * @param sysMenuApi
     * @return
     */
    private boolean hasMenuAuth(Collection<BaseAuth> authorities, SysMenuApi sysMenuApi){
        //基于自己的权限
        if (MenuTypeConstant.AUTH_TYPE.OWNER.equals(sysMenuApi.getAuthType())){
            Long apiId = sysMenuApi.getId();
            return getSysMenuAuths().stream()
                    .filter(a -> a.getApiId().equals(apiId))
                    .map(a->new BaseAuth(a.getAuthority(),a.getDescription()))
                    .anyMatch(authorities::contains);
        } else if (MenuTypeConstant.AUTH_TYPE.PARENT.equals(sysMenuApi.getAuthType())){
            //基于父菜单的权限
            Long parentId = sysMenuApi.getParentId();
            Optional<SysMenuApi> first = getApiMenus().stream().filter(m -> m.getId().equals(parentId)).findFirst();
            if (!first.isPresent()){
                //已经到顶了，找不到 top对应的parent的菜单
                log.info("上层追溯到根菜单，仍未找到对应的权限");
                return false;
            }
            return hasMenuAuth(authorities,first.get());
        } else {
            throw new RuntimeException("菜单权限类型错误: "+sysMenuApi.getAuthType());
        }
    }

    /**
     * 获取菜单列表
     * @return
     */
    private List<SysMenuRoute> getMenuRoutes(){
        if (!isCache){
            return sysMenuRouteMpService.list();
        }
        return authMenuStore.getRouteMenus(() -> sysMenuRouteMpService.list());
    }
    /**
     * 获取菜单列表
     * @return
     */
    private List<SysMenuApi> getApiMenus(){
        if (!isCache){
            return sysMenuPathMpService.list();
        }
        return authMenuStore.getApiMenus(() -> sysMenuPathMpService.list());
    }
    /**
     * 获取菜单权限
     * @return
     */
    private List<SysAuthMenu> getSysMenuAuths(){
        if (!isCache){
            return menuAuthService.list();
        }
        return authMenuStore.getAuthMenus(() -> menuAuthService.list());
    }
}
