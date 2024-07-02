package com.kewen.framework.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kewen.framework.auth.security.extension.SecurityResultConverter;
import com.kewen.framework.auth.security.service.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author kewen
 * @descrpition 权限返回配置，可以继承并实现
 * @since 2024-07-02
 */
public class AuthSuccessFailedDeniedHandler implements AuthCompositeHandler {

    @Autowired
    SecurityResultConverter securityResultConverter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        writeResponseBody(response,securityResultConverter.convertFailed(500,accessDeniedException));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        writeResponseBody(response,securityResultConverter.convertFailed(500,exception));
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser){
            SecurityUser user = (SecurityUser) principal;
            user.setToken(request.getSession().getId());
            //清空密码
            user.setPassword("");
        }
        writeResponseBody(response,securityResultConverter.convertSuccess(principal));
    }


    private void writeResponseBody(HttpServletResponse response,Object result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }

}
