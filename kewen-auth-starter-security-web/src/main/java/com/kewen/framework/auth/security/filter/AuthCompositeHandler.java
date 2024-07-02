package com.kewen.framework.auth.security.filter;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public interface AuthCompositeHandler extends AuthenticationSuccessHandler, AuthenticationFailureHandler, AccessDeniedHandler {

}
