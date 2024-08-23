package com.kewen.framework.auth.security.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * 全局异常处理过滤器，在SpringSecurity的过滤器链之前执行，
 * @author kewen
 * @since 2024-07-10
 */
public class WebGlobalExceptionHandlerFilter  implements BeforeSecurityFilter {

    private static final Logger log = LoggerFactory.getLogger(WebGlobalExceptionHandlerFilter.class);
    HandlerExceptionResolver handlerExceptionResolver;

    public WebGlobalExceptionHandlerFilter(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/error")) {
            log.error("又跑到了错误页面");
            resolver(request,response,new RuntimeException("没有此页面 /error"));
        }
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex){
            log.warn("security前处理返回异常，一般说明SpringSecurity过滤器出异常了");
            //这里不能再往外抛了，会导致tomcat的重定向
            //throw ex;
            resolver(request,response,ex);
        }
    }
    private void resolver(HttpServletRequest request, HttpServletResponse response, Exception ex){
        ModelAndView modelAndView = handlerExceptionResolver.resolveException(request, response, null, ex);
        if (modelAndView == null) {
            log.warn("返回无处理解析器，按照默认返回");
            response.setContentType("application/json;charset=utf-8");
            try {
                PrintWriter out = response.getWriter();
                out.write("返回异常，且没有配置返回解析，异常信息：" + ex.getMessage());
                out.flush();
                out.close();
            } catch (IOException e) {
                throw new AuthenticationServiceException("BeforeSecurityFilter 解析返回异常", e);
            }
        }
    }
}
