package com.kewen.framework.boot.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("kewen-framework.auth.data-range-database-field")
public class DataRangeDatabaseFieldProperties {
    /**
     * 业务关联表名
     */
    private String tableName;
    /**
     * 业务ID存储字段名
     */
    private String dataIdColumn;

    /**
     * 权限存储字段名
     */
    private String authorityColumn;
}
