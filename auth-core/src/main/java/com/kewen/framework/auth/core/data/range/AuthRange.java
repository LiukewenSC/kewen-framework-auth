package com.kewen.framework.auth.core.data.range;

import com.kewen.framework.auth.core.entity.BaseAuth;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * 范围查询定义
 * @author kewen
 * @since 2024-08-08
 */
@Data
@Accessors(chain = true)
class AuthRange {
    /**
     * 应用/模块标识
     */
    private String businessFunction;
    /**
     * 操作 unified  modify update 等
     */
    private String operate;
    /**
     * 表名，在多个表联查的时候指定哪一个表执行
     */
    private String table;
    /**
     * 表别名
     */
    private String tableAlias;
    /**
     * 业务主键名 一般为 id
     */
    private String dataIdColumn;

    /**
     * 用户权限字符串
     */
    private Collection<BaseAuth> authorities;


    /**
     * 权限匹配方式， IN或者exists
     */
    private MatchMethod matchMethod;
}
