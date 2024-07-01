package com.kewen.framework.auth.core.annotation.menu;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @descrpition 菜单权限路径注解 加在controller切面
 * @author kewen
 * @since 2022-11-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthCheckMenuAccess {

    /**
     * 路径，为空则以输入Controller的RequestMapping为准
     * @return
     */
    @AliasFor("value")
    String url() default "";

    @AliasFor("url")
    String value() default "";
}
