package com.kewen.framework.boot.auth.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.AuthDataService;
import com.kewen.framework.auth.core.annotation.data.range.AuthDataTable;
import com.kewen.framework.auth.rabc.RabcAnnotationAuthHandler;
import com.kewen.framework.auth.rabc.composite.SysAuthDataComposite;
import com.kewen.framework.auth.rabc.composite.SysAuthMenuComposite;
import com.kewen.framework.boot.auth.init.InitMenuAuthCommandLineRunner;
import com.kewen.framework.boot.auth.properties.DataRangeDatabaseFieldProperties;
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
        RabcAnnotationAuthHandler handler = new RabcAnnotationAuthHandler();
        handler.setAuthDataTable(dataRangeDatabaseField());
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
    public AuthDataService authDataService(AnnotationAuthHandler annotationAuthHandler){
        AuthDataService authDataService = new AuthDataService();
        authDataService.setAnnotationAuthHandler(annotationAuthHandler);
        return authDataService;
    }

    @Autowired
    SysAuthMenuComposite sysAuthMenuComposite;
    @Autowired
    SysAuthDataComposite sysAuthDataComposite;

    /**
     * 数据权限数据库字段
     * @return
     */
    @Bean
    AuthDataTable dataRangeDatabaseField(){
        AuthDataTable databaseField = new AuthDataTable();

        /* todo 目前因为没有做 @AuthDataRange 和 @AuthDataAuthEdit的统一，因此此处是有问题的，暂时去掉只有范围查询的校验
        databaseField.setTableName(dataRangeDatabaseFieldProperties.getTableName());
        databaseField.setDataIdColumn(dataRangeDatabaseFieldProperties.getDataIdColumn());
        databaseField.setAuthorityColumn(dataRangeDatabaseFieldProperties.getAuthorityColumn());
        */
        databaseField.setTableName("sys_auth_data");
        databaseField.setDataIdColumn("id");
        databaseField.setAuthorityColumn("authority");
        return databaseField;
    }

    @Bean
    InitMenuAuthCommandLineRunner initMenuAuth(){
        return new InitMenuAuthCommandLineRunner();
    }

}
