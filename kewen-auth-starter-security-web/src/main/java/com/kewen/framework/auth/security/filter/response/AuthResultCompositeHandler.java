package com.kewen.framework.auth.security.filter.response;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
/**
 * @author kewen
 * @descrpition 自定义认证授权返回结构
 * @since 2024-07-02
 */
public interface AuthResultCompositeHandler extends AuthenticationSuccessHandler, AuthenticationFailureHandler, AccessDeniedHandler {

}
