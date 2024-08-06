package com.kewen.framework.auth.security.response;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功处理器
 * @author kewen
 * @since 2024-07-04
 */
public interface SecurityAuthenticationSuccessHandler extends AuthenticationSuccessHandler , LogoutSuccessHandler {


    /**
     * 认证成功的数据处理
     */
    @Override
    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException;

    /**
     * 退出登录
     */
    @Override
    void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException;
}
