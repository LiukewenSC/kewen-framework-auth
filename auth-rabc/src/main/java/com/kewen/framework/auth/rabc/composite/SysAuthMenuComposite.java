package com.kewen.framework.auth.rabc.composite;



import com.kewen.framework.auth.core.entity.BaseAuth;
import com.kewen.framework.auth.rabc.model.req.MenuApiSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuApiAndAuthResp;
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
     * @param showDeleted 显示已删除的
     * @return 树形结构的菜单返回对象
     */
    List<MenuApiAndAuthResp> getApiAuthMenuTree(boolean showDeleted);

    /**
     * 获取权限集的请求菜单树
     * @param authorities
     * @return
     */
    List<MenuApiAndAuthResp> getApiAuthMenuTree(Collection<BaseAuth> authorities);

    /**
     * 获取所有菜单路由树（管理使用）
     * @return
     */
    List<MenuRouteResp> getRouteAuthMenuTree(boolean showDeleted);
    /**
     * 获取权限集对应的有权限的路由
     * @param authorities
     * @return
     */
    List<MenuRouteResp> getRouteAuthMenuTree(Collection<BaseAuth> authorities);


    /**
     * 编辑菜单权限
     * @param apiId 菜单id
     * @param auths 权限结构
     */
    void editMenuAuthorities(Long apiId, Collection<BaseAuth> auths);

    void updateMenu(MenuApiSaveReq req);

}
