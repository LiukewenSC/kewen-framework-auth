package com.kewen.framework.auth.rabc.composite;



import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.rabc.model.req.MenuSaveReq;
import com.kewen.framework.auth.rabc.model.resp.MenuAuthResp;
import com.kewen.framework.auth.rabc.model.resp.MenuResp;

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
     * @param url 菜单url链接
     * @return 是否有权限
     */
    boolean hasMenuAuth(Collection<BaseAuth> authorities, String url) ;

    /**
     * 获取树形结构菜单
     * @return 树形结构的菜单返回对象
     */
    List<MenuAuthResp> getMenuTree();

    /**
     * 获取当前用户有权限的菜单
     * @param authorities
     * @return
     */
    List<MenuResp> getCurrentUserMenuTree(Collection<BaseAuth> authorities);


    /**
     * 编辑菜单权限
     * @param menuId 菜单id
     * @param auths 权限结构
     */
    void editMenuAuthorities(Long menuId, Collection<BaseAuth> auths);



    void addMenu(MenuSaveReq req);
    void updateMenu(MenuSaveReq req);
    void deleteMenu(Long req);

}
