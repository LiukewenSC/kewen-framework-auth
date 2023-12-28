package com.kewen.framework.boot.auth.config;

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
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(menuAccessInterceptor);
    }
}
