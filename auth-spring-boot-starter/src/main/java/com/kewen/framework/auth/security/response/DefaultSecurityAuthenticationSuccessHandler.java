package com.kewen.framework.auth.security.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kewen.framework.auth.security.model.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * 认证成功处理器
 * @author kewen
 * @since 2024-07-04
 */
public class DefaultSecurityAuthenticationSuccessHandler implements SecurityAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultSecurityAuthenticationSuccessHandler.class);
    private ObjectMapper objectMapper;
    private AuthenticationSuccessResultResolver resultResolver;

    public DefaultSecurityAuthenticationSuccessHandler(AuthenticationSuccessResultResolver resultResolver , ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.resultResolver = resultResolver;
    }

    /**
     * 认证成功的数据处理，这里处理了之后就返回了
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof SecurityUser)){
            throw new AuthenticationServiceException("Principal is not a SecurityUser");
        }
        SecurityUser user = (SecurityUser) principal;
        user.setToken(request.getSession().getId());
        user.setLoginTime(LocalDateTime.now());
        //清空密码
        user.setPassword("");
        Object result =  resultResolver.resolver(request,response,user);
        writeResponseBody(response, result);
    }
    private void writeResponseBody(HttpServletResponse response,Object result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(result));
        out.flush();
        out.close();
    }

    /**
     * 退出登录返回
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object result = resultResolver.resolver(request,response,null);
        writeResponseBody(response, result);
    }
}
