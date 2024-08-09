package com.kewen.framework.boot.auth.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 *  权限表定义类
 *  有了子包才有此处配置
 * @author kewen
 * @since 2023-12-28
 */
@Data
@ConfigurationProperties("kewen-framework.auth.auth-data-table")
public class AuthDataTableDefinition {
    /**
     * 业务关联表名
     */
    private String tableName="sys_auth_data";
    /**
     * 业务功能字段名
     */
    private String businessFunctionColumn="business_function";
    /**
     * 业务ID存储字段名
     */
    private String dataIdColumn="data_id";
    /**
     * 操作字段名
     */
    private String operateColumn="operation";


    /**
     * 权限存储字段名
     */
    private String authorityColumn="authority";
    /**
     * 权限描述字段名
     */
    private String descriptionColumn="description";
}
