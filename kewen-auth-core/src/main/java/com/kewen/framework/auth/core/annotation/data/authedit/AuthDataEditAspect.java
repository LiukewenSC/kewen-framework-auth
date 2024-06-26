package com.kewen.framework.auth.core.annotation.data.authedit;


import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.data.AuthEditDataAuth;
import com.kewen.framework.auth.core.exception.AuthorizationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author kewen
 * @descrpition 编辑权限切面
 * @since 2022-12-19
 */
@Aspect
public class AuthDataEditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuthDataEditAspect.class);


    private AnnotationAuthHandler annotationAuthAdaptor;



    @Pointcut("@annotation(com.kewen.framework.auth.core.annotation.data.AuthEditDataAuth)")
    public void pointcut(){

    }

    @Around(value = "pointcut() && @annotation(checkDataAuthEdit)",argNames = "joinPoint,checkDataAuthEdit")
    public Object around(ProceedingJoinPoint joinPoint, AuthEditDataAuth checkDataAuthEdit){
        log.info("编辑业务权限，拦截方法:{}",joinPoint.toString());
        Object[] args = joinPoint.getArgs();
        if (args==null){
            throw new AuthorizationException("参数不能为空");
        }
        Optional<AuthDataEditBusiness> first = Arrays.stream(args)
                .filter(a -> a instanceof AuthDataEditBusiness)
                .map(a ->(AuthDataEditBusiness)a)
                .findFirst();
        if (!first.isPresent()){
            throw new AuthorizationException("参数没有找到接口 AuthDataEditBusiness 实现类");
        }
        AuthDataEditBusiness authDataEditBusiness = first.get();

        annotationAuthAdaptor.editDataAuths(
                authDataEditBusiness.getDataId(),
                checkDataAuthEdit.module(),
                checkDataAuthEdit.operate(),
                authDataEditBusiness.getAuthObject().listBaseAuth());

        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("执行编辑失败"+throwable.getMessage(),throwable);
            throw new RuntimeException(throwable.getMessage(),throwable);
        }
    }


    public void setAnnotationAuthAdaptor(AnnotationAuthHandler annotationAuthAdaptor) {
        this.annotationAuthAdaptor = annotationAuthAdaptor;
    }
}
