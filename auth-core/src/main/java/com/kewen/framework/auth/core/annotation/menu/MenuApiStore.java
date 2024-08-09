package com.kewen.framework.auth.core.annotation.menu;

import java.util.List;

/**
 * 菜单API入库类
 *
 * @author kewen
 * @since 2024-08-09
 */
public interface MenuApiStore<T> {

    /**
     * 获取所有的API菜单列表
     * 不需要parentPath填值
     * @return
     */
    List<MenuApiEntity<T>> list();

    /**
     * 保存API列表入库
     * @param apiEntities
     */
    void saveBatch(List<MenuApiEntity<T>> apiEntities);

    /**
     * 获取根的ID，因为菜单API是树形结构的，必然有树根的parentId，一般为 0L 或者null
     * @return
     */
    T getRootParentId();

    /**
     * 生成菜单id，
     * @return
     */
    T generateId();

}
