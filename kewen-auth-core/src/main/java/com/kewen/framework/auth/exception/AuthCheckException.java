package com.kewen.framework.auth.exception;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-04
 */
public class AuthCheckException extends  RuntimeException{
    public AuthCheckException() {
    }

    public AuthCheckException(String message) {
        super("权限校验异常： "+message);
    }

    public AuthCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthCheckException(Throwable cause) {
        super(cause);
    }

    public AuthCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
