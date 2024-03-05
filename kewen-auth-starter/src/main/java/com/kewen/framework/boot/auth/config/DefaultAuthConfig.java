package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.core.context.UserAuthContextContainer;
import com.kewen.framework.auth.support.ThreadLocalUserAuthContextContainer;
import com.kewen.framework.auth.extension.model.DefaultAuthObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
     * 用户权限上下文容器，默认注入ThreadLocal类型的
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(UserAuthContextContainer.class)
    UserAuthContextContainer<DefaultAuthObject> userAuthContextContainer(){
        return new ThreadLocalUserAuthContextContainer();
    }

}
