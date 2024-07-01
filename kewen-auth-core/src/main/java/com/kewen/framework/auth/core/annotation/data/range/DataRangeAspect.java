package com.kewen.framework.auth.core.annotation.data.range;


import com.kewen.framework.auth.core.annotation.data.AuthDataRange;
import com.kewen.framework.auth.core.context.AuthUserContext;
import com.kewen.framework.auth.core.model.BaseAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Collection;

/**
 * @descrpition 切面拦截注解 {@link AuthDataRange} 将注解对应的信息写入权限范围上下文中，供mapper拦截器中使用
 * @author kewen
 * @since 2022-11-24 17:26
 */
@Aspect
public class DataRangeAspect {

    @Pointcut("@annotation(com.kewen.framework.auth.core.annotation.data.AuthDataRange)")
    public void jointPoint() {

    }

    @Around("jointPoint() && @annotation(checkAuthDataRange)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, AuthDataRange checkAuthDataRange) throws Throwable {

        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        DataRangeContext.AuthRange authRange = new DataRangeContext.AuthRange()
                .setModule(checkAuthDataRange.module())
                .setOperate(checkAuthDataRange.operate())
                .setTableAlias(checkAuthDataRange.tableAlias())
                .setDataColumn(checkAuthDataRange.businessColumn())
                .setAuthorities(auths)
                .setMatchMethod(checkAuthDataRange.matchMethod())
                ;

        DataRangeContext.set(authRange);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            DataRangeContext.clear();
        }

    }

}
