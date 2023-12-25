package com.kewen.framework.auth.converter;

import com.kewen.framework.auth.core.annotation.AnnotationAuthAdaptor;
import com.kewen.framework.auth.core.annotation.model.AuthEntity;
import com.kewen.framework.auth.model.Menu;

import java.util.Collection;
import java.util.List;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
public interface ConverterAdaptor<E extends AuthEntity,ID> extends AnnotationAuthAdaptor<E,ID>  {


    /**
     * 是否有菜单编辑权限（编辑整个菜单）
     * 菜单的编辑就包括了菜单本身的信息和菜单权限
     * @param authorities
     * @return
     */
    boolean hasMenuEditAuth(Collection<String> authorities);

    /**
     * 编辑菜单的访问权限
     * @param menuId 菜单id
     * @param authority 权限结构
     */
    void editMenuAuths(ID menuId, List<E> authority);


    /**
     * 编辑菜单信息
     * @param menu
     */
    void editMenuInfo( Menu menu);


}
