package com.kewen.framework.auth.core.annotation.data;

import com.kewen.framework.auth.core.annotation.data.range.MatchMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于当前权限范围查询注解，在查询数据的时候关联数据的权限表以及用户权限体系，检查用户是否拥有数据配置的权限
 * 在mapper拦截 ，用于数据权限列表查询
 * 例如 拼接语句：
 * select * from ${business_table} where
 *   ${business_table}.{business_id} in
 *      (   select data_id from sys_auth_data
 *          where business_function=#{business_function} and operate=#{operate} and authority in ( #{用户权限} )
 *      )
 * 拼接 where后面部分，where前半部分为业务定义的sql，本增强只是在后加上 and 的权限查询语句，避免业务中都需要主动关联权限表匹配，
 * 实现逻辑解耦
 * where后有条件也不用担心，会自动加上and
 * in (select %s from %s where business_function='%s' and operate= '%s' and %s in ( %s ) )
 * @author kewen
 * @since 2022-11-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthDataRange {

    /**
     * 业务功能
     * @return
     */
    String businessFunction() ;


    /**
     * 表名多表联查时用于指定是和哪一张表关联
     * @return
     */
    String table() default "";

    /**
     * 业务主键column名 用于拼接 table.id
     * @return
     */
    String dataIdColumn() default "id";

    /**
     * 默认统一的
     * @return 返回操作类型
     */
    String operate() default "unified";

    /**
     * 条件匹配方式 in/exists
     * 关联原则 小表驱动大表
     * 默认通过in的方式，当权限表中数据大时应该采用exists方式
     * //todo 暂未实现
     * @return
     */
    MatchMethod matchMethod() default MatchMethod.IN;

}