package com.kewen.framework.auth.core.annotation.data.range;



import com.kewen.framework.auth.core.model.BaseAuth;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * @descrpition 权限范围上下文，用于
 * @author kewen
 * @since 2022-11-23 17:10
 */
public class DataRangeContext {

    private static ThreadLocal<AuthRange> local = new InheritableThreadLocal<>();

    public static AuthRange get(){
        return local.get();
    }
    public static void set(AuthRange authRange){
        local.set(authRange);
    }
    public static void clear(){
        local.remove();
    }

    @Data
    @Accessors(chain = true)
    public static class AuthRange {
        /**
         * 应用/模块标识
         */
        private String businessFunction;
        /**
         * 操作 unified  modify update 等
         */
        private String operate;
        /**
         * 表别名
         */
        private String tableAlias;
        /**
         * 业务主键名 一般为 id
         */
        private String dataColumn;

        /**
         * 用户权限字符串
         */
        private Collection<BaseAuth> authorities;


        /**
         * 权限匹配方式， IN或者exists
         */
        private MatchMethod matchMethod;
    }

}
