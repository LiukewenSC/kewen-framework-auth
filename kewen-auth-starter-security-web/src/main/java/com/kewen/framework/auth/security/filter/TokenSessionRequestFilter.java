package com.kewen.framework.auth.security.filter;

import com.kewen.framework.auth.security.filter.token.TokenSessionHttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * token过滤器，构造token的请求体。
 * 这里不要去处理 上下文相关的，上下文相关的应该在AuthticationHoder中处理,因为SpringSecurity会默认将session中的用户解析到Authentication中
 * @author kewen
 * @since 2024-07-02
 */
public class TokenSessionRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //封装 token的session
        TokenSessionHttpServletRequest tokenSessionHttpServletRequest = new TokenSessionHttpServletRequest(request);
        filterChain.doFilter(tokenSessionHttpServletRequest, response);
    }
}
