package com.kewen.framework.auth.core.annotation.menu;

import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.annotation.AnnotationAuthAdaptor;
import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import com.kewen.framework.auth.exception.AuthCheckException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @descrpition 菜单权限校验拦截器，校验请求在菜单中配置的权限，
 *      当Controller中加入注解 {@link CheckMenuAccess} 时生效，否则直接跳过校验
 * @author kewen
 * @since 2022-11-25
 */
@Component
public class MenuAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private AnnotationAuthAdaptor authAdaptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //不兼容的方法，即不是（Controller对应的方法）不做拦截
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        CheckMenuAccess checkEndpoint = handlerMethod.getMethodAnnotation(CheckMenuAccess.class);
        //未添加权限注解，直接跳过校验
        if (checkEndpoint ==null){
            return true;
        }
        //校验url，优先校验注解url，如果为空则校验请求路径（即controller的Path）
        String url= checkEndpoint.url();
        if (StringUtils.isBlank(url)) {
            url = request.getRequestURI();
        }
        //获取当前用户的权限
        IAuthObject authObject = CurrentUserAuthContext.getAuthObject();

        //检查是否有菜单访问权限
        boolean hasMenuAuth = authAdaptor.hasMenuAccessAuth(
                authObject,
                url
        );
        if (!hasMenuAuth){
            throw new AuthCheckException("没有菜单访问权限");
        }
        return true;
    }
}
