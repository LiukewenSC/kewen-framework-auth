package com.kewen.framework.auth.security.before;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在Security之前执行的过滤器
 * @author kewen
 * @since 2024-07-10
 */
public interface BeforeSecurityFilter  {

    /**
     * 执行过滤器
     * @param request
     * @param response
     * @param filterChain
     */
    void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) ;
}
