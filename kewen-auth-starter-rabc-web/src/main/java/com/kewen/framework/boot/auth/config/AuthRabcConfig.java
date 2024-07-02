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
@MapperScan({
        "com.kewen.framework.auth.rabc.mp.mapper",
        "com.kewen.framework.auth.rabc.composite.mapper"
})
@ComponentScan({
        "com.kewen.framework.auth.rabc.mp.service.impl",
        "com.kewen.framework.auth.rabc.composite",
        "com.kewen.framework.auth.rabc.controller"
})
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
    @Bean
    @ConditionalOnMissingBean(AnnotationAuthHandler.class)
    public AnnotationAuthHandler annotationAuthHandler(){
        AnnotationAuthHandlerImpl handler = new AnnotationAuthHandlerImpl();
        handler.setDataRangeDatabaseField(dataRangeDatabaseField);
        handler.setSysAuthDataComposite(sysAuthDataComposite);
        handler.setSysAuthMenuComposite(sysAuthMenuComposite);
        return handler;
    }

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;
    @Autowired
    SysAuthDataComposite sysAuthDataComposite;

    @Autowired
    DataRangeDatabaseField dataRangeDatabaseField;

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

}
