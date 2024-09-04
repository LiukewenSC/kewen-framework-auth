package com.kewen.framework.boot.auth.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;


/**
 *  权限表定义类
 *  有了子包才有此处配置
 * @author kewen
 * @since 2023-12-28
 */
@ConfigurationProperties("kewen-framework.auth.auth-data-table")
public class AuthDataTableProperties {
    /**
     * 业务关联表名
     */
    private String tableName="sys_auth_data";
    /**
     * 主键id字段名
     */
    private String idColumn="id";
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
    private String operateColumn="operate";


    /**
     * 权限存储字段名
     */
    private String authorityColumn="authority";
    /**
     * 权限描述字段名
     */
    private String descriptionColumn="description";

    public String getTableName() {
        return tableName;
    }

    public AuthDataTableProperties setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public AuthDataTableProperties setIdColumn(String idColumn) {
        this.idColumn = idColumn;
        return this;
    }

    public String getBusinessFunctionColumn() {
        return businessFunctionColumn;
    }

    public AuthDataTableProperties setBusinessFunctionColumn(String businessFunctionColumn) {
        this.businessFunctionColumn = businessFunctionColumn;
        return this;
    }

    public String getDataIdColumn() {
        return dataIdColumn;
    }

    public AuthDataTableProperties setDataIdColumn(String dataIdColumn) {
        this.dataIdColumn = dataIdColumn;
        return this;
    }

    public String getOperateColumn() {
        return operateColumn;
    }

    public AuthDataTableProperties setOperateColumn(String operateColumn) {
        this.operateColumn = operateColumn;
        return this;
    }

    public String getAuthorityColumn() {
        return authorityColumn;
    }

    public AuthDataTableProperties setAuthorityColumn(String authorityColumn) {
        this.authorityColumn = authorityColumn;
        return this;
    }

    public String getDescriptionColumn() {
        return descriptionColumn;
    }

    public AuthDataTableProperties setDescriptionColumn(String descriptionColumn) {
        this.descriptionColumn = descriptionColumn;
        return this;
    }
}
