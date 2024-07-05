package com.kewen.framework.auth.security.filter;

import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.security.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * 用户权限上下文，过滤器，添加用户权限上下文，这个是在登录之后进行
 * 即在{@link com.kewen.framework.auth.security.filter.JsonLoginFilter}之后
 *
 * @author kewen
 * @since 2024-07-02
 */
public class AuthUserContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .ifPresent(principal -> {
                    if (principal instanceof SecurityUser) {
                        SecurityUser securityUser = (SecurityUser) principal;
                        AuthUserContext.setAuthObject(securityUser.getAuthObject());
                    }
                });
        filterChain.doFilter(request, response);
    }
}
