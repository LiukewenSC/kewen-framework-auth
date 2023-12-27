package com.kewen.framework.auth.core;

import java.util.Collection;

/**
 * 权限体接口，存放权限体的结构就在这里
 * 如 users roles depts positions 等
 * @author kewen
 * @since 2023-12-27
 */
public interface IAuthObject {

    /**
     * 获取得到基本权限
     * @return
     */
    Collection<BaseAuth> listBaseAuth();

    void setPropertiesFromBaseAuth(Collection<BaseAuth> baseAuths);

}
