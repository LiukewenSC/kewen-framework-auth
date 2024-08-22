package com.kewen.framework.auth.rabc.extension;


import com.kewen.framework.auth.rabc.model.req.UpdatePasswordReq;

/**
 * 
 * @author kewen
 * @since 2024-08-22
 */
public interface RabcCurrentUserService {

    /**
     * 更新密码
     * @param req
     */
    void updatePassword(UpdatePasswordReq req);
}
