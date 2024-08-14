package com.kewen.framework.auth.core.annotation.data;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据范围数据库字段定义
 * @author kewen
 * @since 2023-12-04
 */
@Data
@Accessors(chain = true)
public class AuthDataDO<ID> {

    private ID id;
    /**
     * 业务功能字段名
     */
    private String businessFunction;
    /**
     * 业务ID存储字段名
     */
    private ID dataId;
    /**
     * 操作字段名
     */
    private String operate;


    /**
     * 权限存储字段名
     */
    private String authority;
    /**
     * 权限描述字段名
     */
    private String description;

    public AuthDataDO(String businessFunction, ID dataId, String operate, String authority, String description) {
        this.businessFunction = businessFunction;
        this.dataId = dataId;
        this.operate = operate;
        this.authority = authority;
        this.description = description;
    }
    public AuthDataDO(ID id, String businessFunction, ID dataId, String operate, String authority, String description) {
        this(businessFunction, dataId, operate, authority, description);
        this.id = id;
    }
}
