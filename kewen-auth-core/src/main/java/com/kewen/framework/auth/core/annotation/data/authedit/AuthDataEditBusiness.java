package com.kewen.framework.auth.core.annotation.data.authedit;


import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.support.AbstractAuthEntity;

import java.util.List;

/**
 * @descrpition 编辑权限
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
     * 获取权限对象
     * @return
     */
    IAuthObject  getAuthObject();

}
