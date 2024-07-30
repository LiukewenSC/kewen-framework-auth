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
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface AuthMenu {

    /**
     * 名字
     * @return
     */
    String name();
}
