package com.kewen.framework.auth.core.annotation.data.range;


/**
 * @descrpition 权限范围上下文，用于
 * @author kewen
 * @since 2022-11-23
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

}
