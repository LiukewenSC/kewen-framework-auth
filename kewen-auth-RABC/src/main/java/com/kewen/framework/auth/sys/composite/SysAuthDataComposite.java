package com.kewen.framework.auth.sys.composite;

import com.kewen.framework.auth.core.BaseAuth;

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
     * @param module 模块
     * @param operate 操作
     * @param dataId 业务数据id
     * @return 是否有权限
     */
    boolean hasDataAuth(Collection<BaseAuth> auths, String module, String operate, Long dataId);

    /**
     * 编辑业务数据权限
     * @param dataId 业务数据id
     * @param module 模块
     * @param operate 操作
     * @param auths 权限结构
     */
    void editDataAuths(Long dataId, String module, String operate, Collection<BaseAuth> auths);


}
