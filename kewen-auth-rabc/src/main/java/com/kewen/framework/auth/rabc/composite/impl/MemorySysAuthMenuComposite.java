package com.kewen.framework.auth.rabc.composite.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.model.MenuTypeConstant;
import com.kewen.framework.auth.rabc.model.req.MenuRequestSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuRequestAndAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuRequestResp;
import com.kewen.framework.auth.rabc.model.resp.MenuRouteResp;
import com.kewen.framework.auth.rabc.mp.entity.SysAuthMenu;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRequest;
import com.kewen.framework.auth.rabc.mp.entity.SysMenuRoute;
import com.kewen.framework.auth.rabc.mp.service.SysAuthMenuMpService;
import com.kewen.framework.auth.rabc.mp.service.SysMenuRequestMpService;
import com.kewen.framework.auth.rabc.mp.service.SysMenuRouteMpService;
import com.kewen.framework.auth.rabc.utils.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
@Service
@Slf4j
public class MemorySysAuthMenuComposite implements SysAuthMenuComposite {
    @Autowired
    private SysMenuRequestMpService sysMenuRequestMpService;
    @Autowired
    private SysMenuRouteMpService sysMenuRouteMpService;
    @Autowired
    private SysAuthMenuMpService menuAuthService;

    @Value("${kewen-framework.environment.prod:false}")
    private boolean isCache = false;

    /**
     * 菜单、权限的缓存
     */
    Cache<Object, Object> cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();




    @Override
    public boolean hasMenuAuth(Collection<BaseAuth> authorities, String requestPath) {
        //SysMenuRequest SysMenuRequest = menuService.getMenuByUrl(url);
        Optional<SysMenuRequest> sysMenuOptional = getMenuRequests().stream().filter(m -> Objects.equals(m.getPath(), requestPath)).findFirst();
        if (!sysMenuOptional.isPresent()){
            return false;
        }
        SysMenuRequest sysMenuRequest = sysMenuOptional.get();
        return hasMenuAuth(authorities,sysMenuRequest);
    }

    @Override
    public List<MenuRequestAndAuthResp> getMenuRequestAuthTree() {
        List<SysMenuRequest> sysMenus = getMenuRequests();
        Map<Long, List<SysAuthMenu>> authByMenuMap = getSysMenuAuths().stream()
                .collect(Collectors.groupingBy(SysAuthMenu::getRequestId));
        List<MenuRequestAndAuthResp> collect = sysMenus.stream()
                .map(MenuRequestAndAuthResp::of)
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
     */
    @Override
    public List<MenuRequestAndAuthResp> getAuthsMenuRequestAuthTree(Collection<BaseAuth> authorities) {
        //得到全部树
        List<MenuRequestAndAuthResp> trees = getMenuRequestAuthTree();
        //移除掉不是自己的
        TreeUtil.removeIfUnmatch(trees,t ->{
            boolean hasMenuAuth = hasMenuAuth(authorities, t);
            return  !hasMenuAuth;
        });
        return trees;
    }

    /**
     * 获取权限集对应的有权限的路由
     * @return
     */
    @Override
    public List<MenuRouteResp> getAuthsMenuRouteTree(Collection<BaseAuth> authorities) {
        List<MenuRequestAndAuthResp> trees = getAuthsMenuRequestAuthTree(authorities);
        //克隆副本并转换成 MenuRequestResp
        List<MenuRequestResp> requestRespTrees = TreeUtil.convertList(trees, MenuRequestResp.class);

        //获取路由树
        List<SysMenuRoute> menuRoutes = getMenuRoutes();
        List<MenuRouteResp> routeResps = menuRoutes.stream().map(MenuRouteResp::from).collect(Collectors.toList());
        List<MenuRouteResp> routeRespTree = TreeUtil.transfer(routeResps, 0L);

        //移除无用的节点
        //1. 判断MenuRoute对应的请求id是否在拥有的请求树中
        Set<Long> requestIds = requestRespTrees.stream()
                .map(TreeUtil::fetchSubIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        //2. 移除掉无权限的节点  请求地址id为空或者有用的请求id集合包含了当前请求id则都判断为匹配上了，可以展示到前端
        TreeUtil.removeIfUnmatch(routeRespTree,(t)-> !(t.getRequestPathId()==null || requestIds.contains(t.getRequestPathId())));
        return routeRespTree;
    }



    @Override
    public void editMenuAuthorities(Long menuId, Collection<BaseAuth> baseAuths) {
        //移除原有的
        menuAuthService.remove(
                new LambdaQueryWrapper<SysAuthMenu>().eq(SysAuthMenu::getRequestId,menuId)
        );
        //批量插入新的
        if (!CollectionUtils.isEmpty(baseAuths)){
            menuAuthService.saveBatch(
                    baseAuths.stream()
                            .map(a->
                                    new SysAuthMenu()
                                            .setRequestId(menuId)
                                            .setAuthority(a.getAuth())
                                            .setDescription(a.getDescription())
                            ).collect(Collectors.toList())
            );
        }
    }

    @Override
    @Transactional
    public void updateMenu(MenuRequestSaveReq req) {
        sysMenuRequestMpService.updateById(req);
        Long menuId = req.getId();
        editMenuAuthorities(menuId,req.getAuthObject().listBaseAuth());
    }

    /**
     * 校验菜单权限 ，可递归校验，直到追踪到树根
     * @param authorities
     * @param SysMenuRequest
     * @return
     */
    private boolean hasMenuAuth(Collection<BaseAuth> authorities, SysMenuRequest SysMenuRequest){
        //基于自己的权限
        if (MenuTypeConstant.AUTH_TYPE.OWNER.equals(SysMenuRequest.getAuthType())){
            Long menuId = SysMenuRequest.getId();
            return getSysMenuAuths().stream()
                    .filter(a -> a.getRequestId().equals(menuId))
                    .map(a->new BaseAuth(a.getAuthority(),a.getDescription()))
                    .anyMatch(authorities::contains);
        } else if (MenuTypeConstant.AUTH_TYPE.PARENT.equals(SysMenuRequest.getAuthType())){
            //基于父菜单的权限
            Long parentId = SysMenuRequest.getParentId();
            Optional<SysMenuRequest> first = getMenuRequests().stream().filter(m -> m.getId().equals(parentId)).findFirst();
            if (!first.isPresent()){
                //已经到顶了，找不到 top对应的parent的菜单
                log.info("上层追溯到根菜单，仍未找到对应的权限");
                return false;
            }
            return hasMenuAuth(authorities,first.get());
        } else {
            throw new RuntimeException("菜单权限类型错误: "+SysMenuRequest.getAuthType());
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
        try {
            return (List<SysMenuRoute>)cache.get("menuRoutes", new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return sysMenuRouteMpService.list();
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取菜单列表
     * @return
     */
    private List<SysMenuRequest> getMenuRequests(){
        if (!isCache){
            return sysMenuRequestMpService.list();
        }
        try {
            return (List<SysMenuRequest>)cache.get("menuRequests", new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return sysMenuRequestMpService.list();
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
