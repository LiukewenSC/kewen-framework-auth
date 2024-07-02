package com.kewen.framework.auth.security.extension;

public interface SecurityResultConverter {

    Object convertSuccess(Object data);
    Object convertFailed(Integer code, Throwable t);

}
