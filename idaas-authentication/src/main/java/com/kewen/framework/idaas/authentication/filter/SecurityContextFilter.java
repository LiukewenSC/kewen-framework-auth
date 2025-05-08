package com.kewen.framework.idaas.authentication.filter;


import com.kewen.framework.idaas.authentication.context.AuthenticationContext;
import com.kewen.framework.idaas.authentication.context.AuthenticationContextHolder;
import com.kewen.framework.idaas.authentication.model.UserDetails;
import com.kewen.framework.idaas.authentication.store.AuthenticationStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 2025/04/13
 * 上下文过滤器
 * @author kewen
 * @since 1.0.0
 */
@Component
@Order(-100)
public class SecurityContextFilter extends OncePerRequestFilter {

    public static final String AUTHENTICATION = "Authentication";

    @Autowired
    AuthenticationStore authenticationStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute(AUTHENTICATION);
        if (token == null) {
            token = request.getHeader(AUTHENTICATION);
        }
        if (token != null) {
            UserDetails userDetails = authenticationStore.getUserDetails(token);
            AuthenticationContextHolder.setUserContext(
                    new AuthenticationContext().setUserDetails(userDetails)
            );
        }
        filterChain.doFilter(request, response);
    }
}
