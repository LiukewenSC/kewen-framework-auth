package com.kewen.framework.auth.core.entity;


/**
 * 当前登录人
 * @author kewen
 * @since 2024-08-22
 */
public interface CurrentUser {
    String getUsername();
    String getPassword();

    IAuthObject getAuthObject();
}
