package com.kewen.framework.auth.support;

import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;


/**
 * 权限的扩展 接口
 * @author kewen
 * @since 2023-12-26
 */
public interface ExtensionAuthHandler<A extends IAuthObject,ID> extends AnnotationAuthHandler<A,ID> {


    /**
     * 是否有菜单编辑权限（编辑整个菜单）
     * 菜单的编辑就包括了菜单本身的信息和菜单权限
     * @param authObject 权限结构体
     * @return
     */
    boolean hasMenuEditAuth(A authObject);

    /**
     * 编辑菜单的访问权限
     * @param menuId 菜单id
     * @param authObject 权限结构
     */
    void editMenuAuths(ID menuId, A authObject);

    /**
     * 编辑菜单信息
     * @param menu
     */
    //void editMenuInfo( Menu menu);


}
