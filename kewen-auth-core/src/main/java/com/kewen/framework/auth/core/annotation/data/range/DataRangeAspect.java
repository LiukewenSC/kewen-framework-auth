package com.kewen.framework.auth.core.annotation.data.range;


import com.kewen.framework.auth.core.annotation.data.DataRange;
import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import com.kewen.framework.auth.core.BaseAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @descrpition 切面拦截注解 {@link DataRange} 将注解对应的信息写入权限范围上下文中，供mapper拦截器中使用
 * @author kewen
 * @since 2022-11-24 17:26
 */
@Component
@Aspect
public class DataRangeAspect {

    @Pointcut("@annotation(com.kewen.framework.auth.core.annotation.data.DataRange)")
    public void jointPoint() {

    }

    @Around("jointPoint() && @annotation(checkDataRange)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, DataRange checkDataRange) throws Throwable {

        Collection<BaseAuth> auths = CurrentUserAuthContext.getAuths();
        DataRangeContext.AuthRange selectAuth = new DataRangeContext.AuthRange()
                .setModule(checkDataRange.module())
                .setOperate(checkDataRange.operate())
                .setTableAlias(checkDataRange.tableAlias())
                .setBusinessColumn(checkDataRange.businessColumn())
                .setAuthorities(auths)
                .setMatchMethod(checkDataRange.matchMethod())
                ;

        DataRangeContext.set(selectAuth);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            DataRangeContext.clear();
        }

    }

}
