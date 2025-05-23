package com.kewen.framework.auth.core.data.range;


import com.kewen.framework.auth.core.AuthDataRange;
import com.kewen.framework.auth.core.AuthUserContext;
import com.kewen.framework.auth.core.entity.BaseAuth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Collection;

/**
 * @descrpition 切面拦截注解 {@link AuthDataRange} 将注解对应的信息写入权限范围上下文中，供mapper拦截器中使用
 * @author kewen
 * @since 2022-11-24
 */
@Aspect
public class DataRangeAspect {

    @Pointcut("@annotation(com.kewen.framework.auth.core.AuthDataRange)")
    public void jointPoint() {

    }

    @Around("jointPoint() && @annotation(checkAuthDataRange)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, AuthDataRange checkAuthDataRange) throws Throwable {

        Collection<BaseAuth> auths = AuthUserContext.getAuths();
        AuthRange authRange = new AuthRange()
                .setBusinessFunction(checkAuthDataRange.businessFunction())
                .setOperate(checkAuthDataRange.operate())
                .setTable(checkAuthDataRange.table())
                .setTableAlias(checkAuthDataRange.tableAlias())
                .setDataIdColumn(checkAuthDataRange.dataIdColumn())
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
