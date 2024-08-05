package com.kewen.framework.auth.security.annotation;

import java.lang.annotation.*;

/**
 * 允许放行的url
 * @author kewen
 * @since 2024-07-05
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface SecurityIgnore {

}
