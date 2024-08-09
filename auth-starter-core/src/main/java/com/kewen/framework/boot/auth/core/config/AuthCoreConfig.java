package com.kewen.framework.boot.auth.core.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.authedit.AuthDataEditAspect;
import com.kewen.framework.auth.core.annotation.data.edit.DataCheckAspect;
import com.kewen.framework.auth.core.annotation.data.range.AuthDataTable;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeAspect;
import com.kewen.framework.auth.core.annotation.data.range.MybatisDataRangeInterceptor;
import com.kewen.framework.auth.core.annotation.data.range.SqlAuthInject;
import com.kewen.framework.auth.core.annotation.menu.MenuAccessInterceptor;
import com.kewen.framework.auth.core.annotation.menu.MenuApiGeneratorProcessor;
import com.kewen.framework.boot.auth.core.init.InitMenuApiCommandLineRunner;
import com.kewen.framework.boot.auth.core.properties.AuthDataTableDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 权限核心core模块所需要的配置
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
public class AuthCoreConfig {
    /**
     * 注解处理器
     */
    @Autowired
    private AnnotationAuthHandler annotationAuthHandler;


    /*--------------------------------------core.annotation.下配置--------------------------------------*/
    /**
     *
     * 数据编辑权限切面处理
     * @return
     */
    @Bean
    AuthDataEditAspect authDataEditAspect() {
        AuthDataEditAspect aspect = new AuthDataEditAspect();
        aspect.setAnnotationAuthAdaptor(annotationAuthHandler);
        return aspect;
    }

    /**
     * 数据校验切面处理
     * @return
     */
    @Bean
    DataCheckAspect dataCheckAspect(){
        DataCheckAspect aspect = new DataCheckAspect();
        aspect.setAnnotationAuthHandler(annotationAuthHandler);
        return aspect;
    }

    /**
     * 数据范围切面处理
     * @return
     */
    @Bean
    DataRangeAspect dataRangeAspect(){
        return new DataRangeAspect();
    }

    @Autowired
    AuthDataTableDefinition authDataTableDefinition;
    /**
     * 数据权限数据库字段
     * @return
     */
    @Bean
    AuthDataTable authDataTable(){
        AuthDataTable authDataTable = new AuthDataTable();
        authDataTable.setTableName(authDataTableDefinition.getTableName());
        authDataTable.setBusinessFunctionColumn(authDataTableDefinition.getBusinessFunctionColumn());
        authDataTable.setDataIdColumn(authDataTableDefinition.getDataIdColumn());
        authDataTable.setOperateColumn(authDataTableDefinition.getOperateColumn());
        authDataTable.setAuthorityColumn(authDataTableDefinition.getAuthorityColumn());
        authDataTable.setDescriptionColumn(authDataTableDefinition.getDescriptionColumn());
        return authDataTable;
    }

    /**
     * SQL处理
     * @param authDataTable
     * @return
     */
    @Bean
    SqlAuthInject sqlAuthInject(AuthDataTable authDataTable){
        return new SqlAuthInject(authDataTable);
    }
    /**
     * mybatis拦截器实现
     * @return
     */
    @Bean
    MybatisDataRangeInterceptor mybatisDataRangeInterceptor(){
        MybatisDataRangeInterceptor interceptor = new MybatisDataRangeInterceptor();
        //interceptor.setAnnotationAuthHandler(annotationAuthHandler);
        return interceptor;
    }

    /*--------------------------------------core.annotation.menu下配置--------------------------------------*/

    /**
     * 菜单访问控制拦截器
     * @return
     */
    @Bean
    MenuAccessInterceptor menuAccessInterceptor() {
        MenuAccessInterceptor interceptor = new MenuAccessInterceptor();
        interceptor.setAnnotationAuthHandler(annotationAuthHandler);
        return interceptor;
    }

    /**
     * 菜单API生成器
     * @return
     */
    @Bean
    MenuApiGeneratorProcessor menuApiGeneratorProcessor(){
        return new MenuApiGeneratorProcessor();
    }
    /**
     * 菜单API生成调用
     * @return
     */
    @Bean
    InitMenuApiCommandLineRunner initMenuApiCommandLineRunner(MenuApiGeneratorProcessor menuApiGeneratorProcessor){
        InitMenuApiCommandLineRunner runner = new InitMenuApiCommandLineRunner();
        runner.setMenuApiGeneratorProcessor(menuApiGeneratorProcessor);
        return runner;
    }
}
