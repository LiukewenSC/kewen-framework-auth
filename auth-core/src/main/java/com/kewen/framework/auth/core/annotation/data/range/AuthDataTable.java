package com.kewen.framework.auth.core.annotation.data.range;

import lombok.Data;

/**
 * 数据范围数据库字段定义
 * @author kewen
 * @since 2023-12-04 14:41
 */
@Data
public class AuthDataTable {
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
