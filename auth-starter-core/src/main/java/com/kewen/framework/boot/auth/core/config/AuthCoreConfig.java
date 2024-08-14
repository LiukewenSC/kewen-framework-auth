package com.kewen.framework.boot.auth.core.config;

import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.JdbcAuthDataPersistent;
import com.kewen.framework.auth.core.annotation.data.authedit.AuthDataEditAspect;
import com.kewen.framework.auth.core.annotation.data.edit.DataCheckAspect;
import com.kewen.framework.auth.core.annotation.data.AuthDataTable;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeAspect;
import com.kewen.framework.auth.core.annotation.data.range.MybatisDataRangeInterceptor;
import com.kewen.framework.auth.core.annotation.data.range.SqlAuthInject;
import com.kewen.framework.auth.core.annotation.menu.MenuAccessInterceptor;
import com.kewen.framework.auth.core.annotation.menu.MenuApiGeneratorProcessor;
import com.kewen.framework.boot.auth.core.init.InitMenuApiCommandLineRunner;
import com.kewen.framework.boot.auth.core.properties.AuthDataTableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 权限核心core模块所需要的配置
 *
 * @author kewen
 * @since 2023-12-28
 */
@Configuration
@EnableConfigurationProperties(AuthDataTableProperties.class)
public class AuthCoreConfig {
    /**
     * 注解处理器
     */
    @Autowired
    private AnnotationAuthHandler annotationAuthHandler;


    /*--------------------------------------core.annotation.下配置--------------------------------------*/

    /**
     * 数据编辑权限切面处理
     *
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
     *
     * @return
     */
    @Bean
    DataCheckAspect dataCheckAspect() {
        DataCheckAspect aspect = new DataCheckAspect();
        aspect.setAnnotationAuthHandler(annotationAuthHandler);
        return aspect;
    }

    /**
     * 数据范围切面处理
     *
     * @return
     */
    @Bean
    DataRangeAspect dataRangeAspect() {
        return new DataRangeAspect();
    }

    /**
     * 数据权限数据库字段
     *
     * @return
     */
    @Bean
    AuthDataTable authDataTable(AuthDataTableProperties authDataTableProperties) {
        AuthDataTable dataTable = new AuthDataTable();

        dataTable.setTableName(authDataTableProperties.getTableName())
                .setIdColumn(authDataTableProperties.getIdColumn())
                .setBusinessFunctionColumn(authDataTableProperties.getBusinessFunctionColumn())
                .setDataIdColumn(authDataTableProperties.getDataIdColumn())
                .setOperateColumn(authDataTableProperties.getOperateColumn())
                .setAuthorityColumn(authDataTableProperties.getAuthorityColumn())
                .setDescriptionColumn(authDataTableProperties.getDescriptionColumn());
        return dataTable;
    }

    /**
     * SQL处理
     *
     * @param authDataTable
     * @return
     */
    @Bean
    SqlAuthInject sqlAuthInject(AuthDataTable authDataTable) {
        return new SqlAuthInject(authDataTable);
    }

    /**
     * mybatis拦截器实现
     *
     * @return
     */
    @Bean
    MybatisDataRangeInterceptor mybatisDataRangeInterceptor() {
        MybatisDataRangeInterceptor interceptor = new MybatisDataRangeInterceptor();
        //interceptor.setAnnotationAuthHandler(annotationAuthHandler);
        return interceptor;
    }


    /**
     * 处理权限数据的批量插入删除
     *
     * @return
     */
    @Bean
    JdbcAuthDataPersistent jdbcAuthDataPersistent(AuthDataTableProperties authDataTableProperties,JdbcTemplate jdbcTemplate) {
        JdbcAuthDataPersistent jdbcAuthDataPersistent = new JdbcAuthDataPersistent();
        jdbcAuthDataPersistent.setJdbcTemplate(jdbcTemplate);
        jdbcAuthDataPersistent.setAuthDataTable(authDataTable(authDataTableProperties));
        return jdbcAuthDataPersistent;
    }


    /*--------------------------------------core.annotation.menu下配置--------------------------------------*/

    /**
     * 菜单访问控制拦截器
     *
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
     *
     * @return
     */
    @Bean
    MenuApiGeneratorProcessor menuApiGeneratorProcessor() {
        return new MenuApiGeneratorProcessor();
    }

    /**
     * 菜单API生成调用
     *
     * @return
     */
    @Bean
    InitMenuApiCommandLineRunner initMenuApiCommandLineRunner(MenuApiGeneratorProcessor menuApiGeneratorProcessor) {
        InitMenuApiCommandLineRunner runner = new InitMenuApiCommandLineRunner();
        runner.setMenuApiGeneratorProcessor(menuApiGeneratorProcessor);
        return runner;
    }
}
