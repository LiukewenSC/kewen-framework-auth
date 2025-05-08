package com.kewen.framework.idaas.authentication.filter;


import com.alibaba.fastjson.JSONObject;
import com.kewen.framework.idaas.authentication.context.AuthenticationContext;
import com.kewen.framework.idaas.authentication.context.AuthenticationContextHolder;
import com.kewen.framework.idaas.authentication.handler.AuthenticationAdaptor;
import com.kewen.framework.idaas.authentication.model.UserDetails;
import com.kewen.framework.idaas.authentication.store.AuthenticationStore;
import com.kewen.framework.idaas.model.Result;
import org.springframework.beans.factory.ObjectProvider;
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
import java.io.PrintWriter;


/**
 * 2025/04/11
 * 认证过滤器
 *
 * @author kewen
 * @since 1.0.0
 */
@Component
@Order(100)
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    ObjectProvider<AuthenticationAdaptor> authenticationAdaptorObjectProvider;

    @Autowired
    AuthenticationStore authenticationStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // 认证，如果是认证对应的接口，就需要再次认证，即使已经是认证过的；
        UserDetails userDetails = authentication(request);
        if (userDetails != null) {
            saveUserDetails(request,userDetails);
            writeResponse(response, Result.success(userDetails));
            return;
        }

        boolean isAuthenticated = AuthenticationContextHolder.getUserContext() != null;
        if (!isAuthenticated) {
            writeResponse(response, Result.failed("未认证"));
            return;
        }

        filterChain.doFilter(request, response);

    }

    private static void writeResponse(HttpServletResponse response, Result result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(
                JSONObject.toJSONString(result)
        );
        writer.flush();
    }

    private void saveUserDetails(HttpServletRequest request,UserDetails userDetails) {

        authenticationStore.saveUserDetails(userDetails);
        AuthenticationContextHolder.setUserContext(
                new AuthenticationContext().setUserDetails(userDetails)
        );
        HttpSession session = request.getSession();
        session.setAttribute(SecurityContextFilter.AUTHENTICATION, userDetails.getToken());

    }

    private UserDetails authentication(HttpServletRequest request) throws IOException {
        for (AuthenticationAdaptor authenticationAdaptor : authenticationAdaptorObjectProvider) {
            if (authenticationAdaptor.support(request)) {
                UserDetails userDetails = authenticationAdaptor.authentication(request);
                if (userDetails != null) {
                    return userDetails;
                }
            }
        }
        return null;
    }
}
