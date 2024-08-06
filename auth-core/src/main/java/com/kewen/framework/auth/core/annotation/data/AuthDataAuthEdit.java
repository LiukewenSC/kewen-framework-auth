package com.kewen.framework.auth.core.annotation.data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 执行修改业务权限逻辑，记得在这之前要先执行url权限验证,自行控制权限，这里只是封装编辑逻辑
 * 加上注解直接就开始修改业务的权限了，用了这个注解就不用再写权限逻辑，只需要完成写权限的后续逻辑即可
 * @author kewen
 * @since 2022-12-19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthDataAuthEdit {

    /**
     * 模块ID
     * @return 模块ID
     */
    String businessFunction() ;

    /**
     * 操作
     *  默认统一的 对应authority_reference表中的 operate字段，
     *  因为数据库不留空，若没有对操作的要求则默认unified
     *  可以为 unified modify update delete 或者其他自定义的操作，传入时需要匹配对应的操作
     * @return 返回操作类型
     */
    String operate() default "unified";

    /**
     * 在主要方法之前执行，即先执行数据写入，再执行后续业务逻辑
     * @return
     */
    boolean before() default true;
}
