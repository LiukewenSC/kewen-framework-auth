package com.kewen.framework.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.security.model.SecurityUser;
import com.kewen.framework.auth.security.response.AuthenticationSuccessResultResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * 用户权限上下文，过滤器，添加用户权限上下文，这个是在登录之后进行
 * 即在{@link com.kewen.framework.auth.security.filter.JsonLoginFilter}之后
 *
 * @author kewen
 * @since 2024-07-02
 */
public class AuthUserContextFilter extends OncePerRequestFilter {

    private String currentUserUrl;
    private ObjectMapper objectMapper;
    private AuthenticationSuccessResultResolver resultResolver;

    public AuthUserContextFilter(String currentUserUrl, ObjectProvider<AuthenticationSuccessResultResolver> resultResolverProvider , ObjectMapper objectMapper) {
        this.currentUserUrl = currentUserUrl;
        this.resultResolver = resultResolverProvider.getIfAvailable();
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<Object> principalOptional = Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal);

        //如果是查询当前用户，则直接返回，没有查询到则报错需要登录
        if (currentUserUrl.equals(request.getRequestURI())) {
            if (!principalOptional.isPresent() || !(principalOptional.get() instanceof SecurityUser)) {
                throw new AuthenticationServiceException("No SecurityUser found");
            }
            Object currentUser = principalOptional.get();
            Object resolver = resultResolver.resolver(request, response, ((SecurityUser) currentUser));
            writeResponseBody(response,resolver);
            return;
        }
        principalOptional.ifPresent(principal -> {
            if (principal instanceof SecurityUser) {
                SecurityUser securityUser = (SecurityUser) principal;
                AuthUserContext.setAuthObject(securityUser.getAuthObject());
            }
        });


        filterChain.doFilter(request, response);
    }

    private void writeResponseBody(HttpServletResponse response,Object result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(result));
        out.flush();
        out.close();
    }
}
