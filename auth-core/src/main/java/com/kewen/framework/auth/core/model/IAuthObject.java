package com.kewen.framework.auth.core.model;

import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 权限集合体接口，存放权限集合体的结构就在这里
 * 如 users roles depts positions 等
 * @author kewen
 * @since 2023-12-27
 */
public interface IAuthObject {

    default boolean isEmpty(){
        return CollectionUtils.isEmpty(listBaseAuth());
    }
    /**
     * 获取得到基本权限
     * @return
     */
    Collection<BaseAuth> listBaseAuth();

    /**
     * 通过BaseAuth设置权限体的权限属性值
     * @param baseAuths
     */
    void setProperties(Collection<BaseAuth> baseAuths);

}
