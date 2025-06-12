package com.kewen.framework.idaas.application.exception;

public class CertificationException extends RuntimeException {
    public CertificationException(String message) {
        super(message);
    }

    public CertificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificationException(Throwable cause) {
        super(cause);
    }
}
