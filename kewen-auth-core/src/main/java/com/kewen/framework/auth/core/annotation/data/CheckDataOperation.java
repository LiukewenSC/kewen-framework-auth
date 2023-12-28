package com.kewen.framework.auth.core.annotation.data;


import com.kewen.framework.auth.core.annotation.data.edit.BusinessData;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @descrpition 校验是否有编辑单条数据的权限
 * 实现了此注解会根据传入的模块ID和操作以及业务ID自动判定是否有执行此操作的权限
 *  select business_id from sys_application_auth
 *  where application=#{application} and operate=#{operate} and business_id=#{businessId} and authority in ( #{用户权限} )
 *  limit 1
 * businessId需要关联获取 {@link BusinessData}
 * @author kewen
 * @since 2022-11-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckDataOperation {
    /**
     * 模块ID
     * @return 模块ID
     */
    String module() ;

    /**
     * 操作
     *  默认统一的 对应authority_reference表中的 operate字段，
     *  因为数据库不留空，若没有对操作的要求则默认unified
     *  可以为 unified modify update delete 或者其他自定义的操作，传入时需要匹配对应的操作
     * @return 返回操作类型
     */
    String operate() default "unified";
}
