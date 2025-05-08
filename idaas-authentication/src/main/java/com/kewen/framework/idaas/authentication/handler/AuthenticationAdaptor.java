package com.kewen.framework.idaas.authentication.handler;


import com.kewen.framework.idaas.authentication.model.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 2025/04/13
 * 认证处理器
 * @author kewen
 * @since 1.0.0
 */
public interface AuthenticationAdaptor {
    boolean support(HttpServletRequest request);
    UserDetails authentication(HttpServletRequest request) throws IOException;
}
