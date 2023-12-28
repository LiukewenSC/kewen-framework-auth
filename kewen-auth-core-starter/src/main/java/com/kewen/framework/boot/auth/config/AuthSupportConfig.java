package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.sys.DefaultAnnotationAuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * support实现相关
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
public class AuthSupportConfig {

    /**
     * 注解权限处理器
     * @return
     */
    @Bean
    public AnnotationAuthHandler annotationAuthHandler() {
        return new DefaultAnnotationAuthHandler();
    }


}
