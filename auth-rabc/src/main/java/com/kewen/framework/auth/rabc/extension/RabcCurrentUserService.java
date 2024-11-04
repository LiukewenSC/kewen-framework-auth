package com.kewen.framework.auth.rabc.extension;


import com.kewen.framework.auth.core.entity.CurrentUser;

/**
 *  当前用户登录
 * @author kewen
 * @since 2024-08-22
 */
public interface RabcCurrentUserService<T extends CurrentUser> {


    /**
     * 更新密码
     * @param currentUser 当前登录人
     * @param oldPassword 旧密码，未加密
     * @param newPassword 新密码，未加密
     */
     void updatePassword(T currentUser , String oldPassword, String newPassword);
}
