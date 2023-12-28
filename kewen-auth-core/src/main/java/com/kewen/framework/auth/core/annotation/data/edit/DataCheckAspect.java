package com.kewen.framework.auth.core.annotation.data.edit;


import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.annotation.data.CheckDataOperation;
import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import com.kewen.framework.auth.exception.AuthException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * @descrpition 校验业务权限切面，通过业务id和应用校验权限
 * @author kewen
 * @since 2022-11-25
 */
@Aspect
public class DataCheckAspect {

    private static final Logger log = LoggerFactory.getLogger(DataCheckAspect.class);

    private AnnotationAuthHandler annotationAuthHandler;



    @Pointcut("@annotation(com.kewen.framework.auth.core.annotation.data.CheckDataOperation)")
    public void pointcut(){}

    @Before(value = "pointcut() && @annotation(authAnn)", argNames = "joinPoint,authAnn")
    public void before(JoinPoint joinPoint, CheckDataOperation authAnn){
        log.info("校验用户权限，拦截方法:{}",joinPoint.toString());
        Object[] args = joinPoint.getArgs();
        if (args==null){
            throw new AuthException("参数不能为空");
        }
        Optional<Object> first = Arrays.stream(args).filter(a -> a instanceof BusinessData).findFirst();
        if (!first.isPresent()){
            throw new AuthException("参数没有找到接口ApplicationBusiness实现类");
        }
        BusinessData business = (BusinessData) first.get();
        Collection<BaseAuth> auths = CurrentUserAuthContext.getAuths();
        boolean hasAuth = annotationAuthHandler.hasDataOperateAuths(auths, authAnn.module(), authAnn.operate(), business.getDataId());
        if (!hasAuth){
            throw new AuthException("权限校验不通过");
        }
    }

    public void setAnnotationAuthHandler(AnnotationAuthHandler annotationAuthHandler) {
        this.annotationAuthHandler = annotationAuthHandler;
    }
}
