package com.kewen.framework.auth.security.exception;

import org.springframework.security.core.AuthenticationException;

public class NoLoginException extends AuthenticationException {

    public NoLoginException(String msg, Throwable t) {
        super(msg, t);
    }

    public NoLoginException(String msg) {
        super(msg);
    }
}
