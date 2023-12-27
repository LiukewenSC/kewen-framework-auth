package com.kewen.framework.auth.extension;

import com.kewen.framework.auth.core.annotation.AnnotationAuthAdaptor;
import com.kewen.framework.auth.model.IAuthEntity;
import com.kewen.framework.auth.model.Menu;

import java.util.Collection;
import java.util.List;

/**
 * 权限的扩展 接口
 * @author kewen
 * @since 2023-12-26
 */
public interface AuthAdaptorExtension<E extends IAuthEntity,ID> extends AnnotationAuthAdaptor<ID> {


    /**
     * 是否有菜单编辑权限（编辑整个菜单）
     * 菜单的编辑就包括了菜单本身的信息和菜单权限
     * @param auths
     * @return
     */
    boolean hasMenuEditAuth(Collection<E> auths);

    /**
     * 编辑菜单的访问权限
     * @param menuId 菜单id
     * @param auths 权限结构
     */
    void editMenuAuths(ID menuId, List<E> auths);

    /**
     * 编辑菜单信息
     * @param menu
     */
    void editMenuInfo( Menu menu);


}
