package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.sys.AnnotationAuthHandlerImpl;
import com.kewen.framework.auth.sys.composite.SysAuthDataComposite;
import com.kewen.framework.auth.sys.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.sys.composite.impl.MemorySysAuthMenuComposite;
import com.kewen.framework.auth.sys.composite.impl.SysAuthDataCompositeImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *  impl 实现相关
 *  有了子包才有此处配置
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
@MapperScan({"com.kewen.framework.auth.sys.mp.mapper","com.kewen.framework.auth.sys.composite.mapper"})
@ComponentScan("com.kewen.framework.auth.sys.mp.service.impl")
@ConditionalOnClass(AnnotationAuthHandlerImpl.class)
public class AuthImplConfig {

    /**
     * 注解权限处理器
     * 在引入了<kewen-auth-impl> 依赖的情况下配置
     * 在系统中没有自定义AnnotationAuthHandler时配置
     * @return
     */
    @Bean(autowire = Autowire.BY_TYPE)
    @ConditionalOnMissingBean(AnnotationAuthHandler.class)
    /*@Lazy
    public AnnotationAuthHandler annotationAuthHandler() {
        SysAnnotationAuthHandler handler = new SysAnnotationAuthHandler();
        return handler;
    }*/
    public FactoryBean<AnnotationAuthHandler> annotationAuthHandler(){
        return new FactoryBean<AnnotationAuthHandler>() {
            @Override
            public AnnotationAuthHandler getObject() throws Exception {
                return new AnnotationAuthHandlerImpl();
            }

            @Override
            public Class<?> getObjectType() {
                return AnnotationAuthHandler.class;
            }
        };
    }

    /**
     * 业务数据权限组合器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SysAuthDataComposite.class)
    SysAuthDataComposite sysDataAuthComposite(){
        return new SysAuthDataCompositeImpl();
    }

    /**
     * 菜单权限整合服务类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SysAuthMenuComposite.class)
    SysAuthMenuComposite memorySysMenuAuthComposite(){
        return new MemorySysAuthMenuComposite();
    }


}
