package com.kewen.framework.auth.security.filter.response;

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
 * 默认认证授权返回结构
 * @author kewen
 * @since 2024-07-02
 */
public class AuthResultSuccessFailedDeniedHandler implements AuthResultCompositeHandler {

    @Autowired
    SecurityResultConverter securityResultConverter;

    /**
     * 访问异常的处理
     * @param request               that resulted in an <code>AccessDeniedException</code>
     * @param response              so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        writeResponseBody(response,securityResultConverter.convertFailed(505,accessDeniedException));
    }

    /**
     * 认证失败的处理
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        writeResponseBody(response,securityResultConverter.convertFailed(500,exception));
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
