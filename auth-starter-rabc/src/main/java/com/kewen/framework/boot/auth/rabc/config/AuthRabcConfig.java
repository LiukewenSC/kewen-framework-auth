package com.kewen.framework.boot.auth.rabc.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.AuthDataManager;
import com.kewen.framework.auth.rabc.RabcAnnotationAuthHandler;
import com.kewen.framework.auth.rabc.composite.SysAuthDataComposite;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.mp.service.SysMenuApiMpService;
import com.kewen.framework.auth.rabc.extension.RabcMenuApiStore;
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
    SysAuthDataComposite sysAuthDataComposite;
    @Autowired
    SysMenuApiMpService sysMenuApiMpService;
    /**
     * 注解权限处理器
     * 在引入了<kewen-auth-impl> 依赖的情况下配置
     * 在系统中没有自定义AnnotationAuthHandler时配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AnnotationAuthHandler.class)
    public AnnotationAuthHandler annotationAuthHandler(){
        RabcAnnotationAuthHandler handler = new RabcAnnotationAuthHandler();
        handler.setSysAuthDataComposite(sysAuthDataComposite);
        handler.setSysAuthMenuComposite(sysAuthMenuComposite);
        return handler;
    }

    /**
     * 调用服务，可以从这里调用，就不再需要从注解 @AuthDataAuthEdit上执行了，提供了第二种可能
     * @param annotationAuthHandler
     * @return
     */
    @Bean
    public AuthDataManager authDataManager(AnnotationAuthHandler annotationAuthHandler){
        AuthDataManager authDataService = new AuthDataManager();
        authDataService.setAnnotationAuthHandler(annotationAuthHandler);
        return authDataService;
    }

    @Bean
    RabcMenuApiStore rabcMenuApiStore(){
        RabcMenuApiStore store = new RabcMenuApiStore();
        store.setSysMenuApiMpService(sysMenuApiMpService);
        return store;
    }

}
