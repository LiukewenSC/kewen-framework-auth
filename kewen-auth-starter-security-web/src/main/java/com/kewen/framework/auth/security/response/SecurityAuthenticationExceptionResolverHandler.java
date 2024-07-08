package com.kewen.framework.auth.security.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 安全异常返回处理，这里就直接加入SpringMVC的异常解析器了，子工程里使用解析器解析吧，统一了SpringSecurity的异常不能通用处理
 *
 * @author kewen
 * @since 2024-07-02
 */
public class SecurityAuthenticationExceptionResolverHandler implements AuthenticationEntryPoint, AuthenticationFailureHandler, AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAuthenticationExceptionResolverHandler.class);

    HandlerExceptionResolver handlerExceptionResolver;

    /**
     * AuthenticationEntryPoint 的实现
     * 处理未进入登录但后续需要验证登录的异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        resolver(request, response, exception);
    }

    /**
     * AccessDeniedHandler 的实现
     * 访问异常的处理
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        resolver(request, response, exception);
    }

    /**
     * 认证失败的处理
     * AuthenticationFailureHandler 的实现
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        resolver(request, response, exception);
    }

    /**
     * 具体解析器的处理逻辑
     */
    private void resolver(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        //拿到容器中配置的HandlerExceptionResolver，并调用其解析逻辑，这个类本身就会扫描`@ControllerAdvice`并加入HandlerExceptionResolver中
        ModelAndView modelAndView = handlerExceptionResolver.resolveException(request, response, null, exception);
        //没有解析到，没有解析到那就默认返回
        if (modelAndView == null) {
            logger.warn("安全异常返回无处理解析器，按照默认返回");
            response.setContentType("application/json;charset=utf-8");
            try {
                PrintWriter out = response.getWriter();
                out.write("Security 安全框架返回异常，且没有配置返回解析，异常信息：" + exception.getMessage());
                out.flush();
                out.close();
            } catch (IOException e) {
                throw new AuthenticationServiceException("SecurityExceptionResolverHandler 解析返回异常", e);
            }
        }
    }

    public SecurityAuthenticationExceptionResolverHandler setHandlerExceptionResolver(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        return this;
    }

}
