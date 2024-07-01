package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;
import com.kewen.framework.auth.rabc.AnnotationAuthHandlerImpl;
import com.kewen.framework.auth.rabc.composite.SysAuthDataComposite;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.auth.rabc.composite.SysUserComposite;
import com.kewen.framework.auth.rabc.composite.impl.MemorySysAuthMenuComposite;
import com.kewen.framework.auth.rabc.composite.impl.SysAuthDataCompositeImpl;
import com.kewen.framework.auth.rabc.composite.impl.SysUserCompositeImpl;
import com.kewen.framework.boot.auth.properties.DataRangeDatabaseFieldProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@MapperScan({"com.kewen.framework.auth.rabc.mp.mapper", "com.kewen.framework.auth.rabc.composite.mapper"})
@ComponentScan("com.kewen.framework.auth.rabc.mp.service.impl")
@ConditionalOnClass(AnnotationAuthHandlerImpl.class)
@EnableConfigurationProperties(DataRangeDatabaseFieldProperties.class)
public class AuthRabcConfig {

    @Autowired
    DataRangeDatabaseFieldProperties dataRangeDatabaseFieldProperties;

    /**
     * 注解权限处理器
     * 在引入了<kewen-auth-impl> 依赖的情况下配置
     * 在系统中没有自定义AnnotationAuthHandler时配置
     * @return
     */
    @Bean(autowire = Autowire.BY_TYPE)
    @ConditionalOnMissingBean(AnnotationAuthHandler.class)
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
     * 数据权限数据库字段
     * @return
     */
    @Bean
    DataRangeDatabaseField dataRangeDatabaseField(){
        DataRangeDatabaseField databaseField = new DataRangeDatabaseField();
        databaseField.setTableName(dataRangeDatabaseFieldProperties.getTableName());
        databaseField.setDataIdColumn(dataRangeDatabaseFieldProperties.getDataIdColumn());
        databaseField.setAuthorityColumn(dataRangeDatabaseFieldProperties.getAuthorityColumn());
        return databaseField;
    }

    /**
     * 业务数据权限组合器
     * @return
     */
    @Bean(autowire = Autowire.BY_TYPE)
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



    /**
     * 用户相关
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SysUserComposite.class)
    SysUserComposite sysUserComposite(){
        return new SysUserCompositeImpl();
    }



}
