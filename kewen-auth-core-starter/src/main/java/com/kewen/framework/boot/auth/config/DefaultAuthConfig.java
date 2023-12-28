package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.context.UserAuthContextContainer;
import com.kewen.framework.auth.sys.DefaultAnnotationAuthHandler;
import com.kewen.framework.auth.support.ThreadLocalUserAuthContextContainer;
import com.kewen.framework.auth.extension.model.DefaultAuthObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author kewen
 * @since 2023-12-26
 */
@Configuration
public class DefaultAuthConfig {

    /**
     * 注解权限处理器
     * @return
     */
    @Bean

    AnnotationAuthHandler annotationAuthHandler() {
        return new DefaultAnnotationAuthHandler();
    }

    @Bean
    UserAuthContextContainer<DefaultAuthObject> defaultCurrentAuthUserContextContainer(){
        return new ThreadLocalUserAuthContextContainer();
    }

}
