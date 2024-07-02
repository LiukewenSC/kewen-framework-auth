package com.kewen.framework.auth.web.config;

import com.kewen.framework.auth.core.annotation.menu.MenuAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限相关web拦截器，主要判定菜单相关
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

    @Autowired
    MenuAccessInterceptor menuAccessInterceptor;

    /**
     * 添加菜单拦截器，拦截请求接口，可以在mvc层鉴权
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(menuAccessInterceptor);
    }
}
