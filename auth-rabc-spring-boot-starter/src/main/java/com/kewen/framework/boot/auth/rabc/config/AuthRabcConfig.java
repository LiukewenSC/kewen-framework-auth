package com.kewen.framework.boot.auth.rabc.config;

import com.kewen.framework.auth.core.data.AuthDataHandler;
import com.kewen.framework.auth.core.AuthDataAdaptor;
import com.kewen.framework.auth.core.data.JdbcAuthDataPersistent;
import com.kewen.framework.auth.rabc.RabcAnnotationAuthHandler;
import com.kewen.framework.auth.rabc.composite.AuthMenuStore;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.composite.impl.MemoryExpiredAuthMenuStore;
import com.kewen.framework.auth.rabc.extension.RabcMenuApiServcie;
import com.kewen.framework.auth.rabc.mp.service.SysMenuApiMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  impl 实现相关
 *  有了子包才有此处配置
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
public class AuthRabcConfig {

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;
    @Autowired
    SysMenuApiMpService sysMenuApiMpService;
    @Autowired
    JdbcAuthDataPersistent jdbcAuthDataPersistent;
    /**
     * 注解权限处理器
     * 在引入了<kewen-auth-impl> 依赖的情况下配置
     * 在系统中没有自定义AnnotationAuthHandler时配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AuthDataHandler.class)
    public RabcAnnotationAuthHandler annotationAuthHandler(){
        RabcAnnotationAuthHandler handler = new RabcAnnotationAuthHandler();
        handler.setSysAuthMenuComposite(sysAuthMenuComposite);
        return handler;
    }

    /**
     * 调用服务，可以从这里调用，就不再需要从注解 @AuthDataAuthEdit上执行了，提供了第二种可能
     * @return
     */
    @Bean
    public AuthDataAdaptor authDateAdaptor(){
        return new AuthDataAdaptor();
    }

    /**
     * 菜单API服务
     * @return
     */
    @Bean
    RabcMenuApiServcie rabcMenuApiStore(){
        RabcMenuApiServcie store = new RabcMenuApiServcie();
        store.setSysMenuApiMpService(sysMenuApiMpService);
        return store;
    }

    /**
     * 菜单存储，可以基于内存或redis实现
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AuthMenuStore.class)
    AuthMenuStore authMenuStore(){
        return new MemoryExpiredAuthMenuStore();
    }

}
