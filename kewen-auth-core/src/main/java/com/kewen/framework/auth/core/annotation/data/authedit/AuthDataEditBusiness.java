package com.kewen.framework.auth.core.annotation.data.authedit;


import com.kewen.framework.auth.core.IAuthObject;

/**
 * @descrpition 数据的 编辑权限
 * @author kewen
 * @since 2022-12-19
 */
public interface AuthDataEditBusiness<ID> {

    /**
     * 获取业务ID
     * @return
     */
    ID getBusinessId();

    /**
     * 获取需要设置的权限集合体
     * @return
     */
    IAuthObject  getAuthObject();

}
