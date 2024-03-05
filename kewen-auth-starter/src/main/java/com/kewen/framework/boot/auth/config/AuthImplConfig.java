package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.sys.SysAnnotationAuthHandler;
import com.kewen.framework.auth.sys.composite.SysDataAuthComposite;
import com.kewen.framework.auth.sys.composite.SysMenuAuthComposite;
import com.kewen.framework.auth.sys.composite.impl.MemorySysMenuAuthComposite;
import com.kewen.framework.auth.sys.composite.impl.SysDataAuthCompositeImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 *  impl 实现相关
 *  有了子包才有此处配置
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
@MapperScan({"com.kewen.framework.auth.sys.mp.mapper","com.kewen.framework.auth.sys.composite.mapper"})
@ComponentScan("com.kewen.framework.auth.sys.mp.service.impl")
@ConditionalOnClass(SysAnnotationAuthHandler.class)
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
                return new SysAnnotationAuthHandler();
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
    @ConditionalOnMissingBean(SysDataAuthComposite.class)
    SysDataAuthComposite sysDataAuthComposite(){
        return new SysDataAuthCompositeImpl();
    }

    /**
     * 菜单权限整合服务类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SysMenuAuthComposite.class)
    SysMenuAuthComposite memorySysMenuAuthComposite(){
        return new MemorySysMenuAuthComposite();
    }


}
