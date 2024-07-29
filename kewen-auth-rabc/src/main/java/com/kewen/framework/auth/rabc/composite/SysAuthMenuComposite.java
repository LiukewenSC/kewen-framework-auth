package com.kewen.framework.auth.rabc.composite;



import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.model.req.MenuRequestSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuRequestAndAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuRouteResp;

import java.util.Collection;
import java.util.List;

/**
 * @descrpition  菜单权限整合服务类
 * @author kewen
 * @since 2022-12-01
 */
public interface SysAuthMenuComposite {
    /**
     * 校验用户的菜单权限
     * @param authorities 用户权限字符串
     * @param requestPath 请求路径
     * @return 是否有权限
     */
    boolean hasMenuAuth(Collection<BaseAuth> authorities, String requestPath) ;

    /**
     * 获取树形结构菜单
     * @return 树形结构的菜单返回对象
     */
    List<MenuRequestAndAuthResp> getMenuRequestAuthTree();

    /**
     * 获取权限集的请求菜单树
     * @param authorities
     * @return
     */
    List<MenuRequestAndAuthResp> getAuthsMenuRequestAuthTree(Collection<BaseAuth> authorities);

    /**
     * 获取权限集对应的有权限的路由
     * @param authorities
     * @return
     */
    List<MenuRouteResp> getAuthsMenuRouteTree(Collection<BaseAuth> authorities);


    /**
     * 编辑菜单权限
     * @param menuId 菜单id
     * @param auths 权限结构
     */
    void editMenuAuthorities(Long menuId, Collection<BaseAuth> auths);

    void updateMenu(MenuRequestSaveReq req);

}
