package com.kewen.framework.auth.rabc.composite;

import com.kewen.framework.auth.core.model.BaseAuth;

import java.util.Collection;

/**
 * 业务数据权限组合器
 * @author kewen
 * @since 2023-12-28
 */
public interface SysAuthDataComposite {

    /**
     * 查询用户手有某个业务操作的权限
     * @param auths 用户权限
     * @param businessFunction 模块
     * @param operate 操作
     * @param dataId 业务数据id
     * @return 是否有权限
     */
    boolean hasDataAuth(Collection<BaseAuth> auths, String businessFunction, String operate, Long dataId);

    /**
     * 编辑业务数据权限
     * @param dataId 业务数据id
     * @param businessFunction 模块
     * @param operate 操作
     * @param auths 权限结构
     */
    void editDataAuths(Long dataId, String businessFunction, String operate, Collection<BaseAuth> auths);


}
