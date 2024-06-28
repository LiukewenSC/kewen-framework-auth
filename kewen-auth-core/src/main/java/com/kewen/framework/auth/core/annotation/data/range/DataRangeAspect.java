package com.kewen.framework.auth.core.annotation.data.range;


import com.kewen.framework.auth.core.annotation.data.DataRange;
import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import com.kewen.framework.auth.core.BaseAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Collection;

/**
 * @descrpition 切面拦截注解 {@link DataRange} 将注解对应的信息写入权限范围上下文中，供mapper拦截器中使用
 * @author kewen
 * @since 2022-11-24 17:26
 */
@Aspect
public class DataRangeAspect {

    @Pointcut("@annotation(com.kewen.framework.auth.core.annotation.data.DataRange)")
    public void jointPoint() {

    }

    @Around("jointPoint() && @annotation(checkDataRange)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, DataRange checkDataRange) throws Throwable {

        Collection<BaseAuth> auths = CurrentUserAuthContext.getAuths();
        DataRangeContext.AuthRange authRange = new DataRangeContext.AuthRange()
                .setModule(checkDataRange.module())
                .setOperate(checkDataRange.operate())
                .setTableAlias(checkDataRange.tableAlias())
                .setDataColumn(checkDataRange.businessColumn())
                .setAuthorities(auths)
                .setMatchMethod(checkDataRange.matchMethod())
                ;

        DataRangeContext.set(authRange);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            DataRangeContext.clear();
        }

    }

}
