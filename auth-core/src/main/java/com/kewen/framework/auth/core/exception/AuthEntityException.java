package com.kewen.framework.auth.core.exception;



/**
 * 权限对象异常
 * @author kewen
 * @since 2024-09-06
 */
public class AuthEntityException extends AuthException {

    private static final long serialVersionUID = -7550464198188755866L;

    public AuthEntityException(String message) {
        super("权限对象异常: "+message);
    }
    public AuthEntityException(String message, Throwable cause) {
        super("权限对象异常: "+message, cause);
    }
}
