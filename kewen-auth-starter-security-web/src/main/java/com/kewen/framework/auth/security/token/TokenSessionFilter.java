package com.kewen.framework.auth.security.token;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenSessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        TokenSessionHttpServletRequest tokenSessionHttpServletRequest = new TokenSessionHttpServletRequest(request);
        
        filterChain.doFilter(tokenSessionHttpServletRequest, response);
    }
}
