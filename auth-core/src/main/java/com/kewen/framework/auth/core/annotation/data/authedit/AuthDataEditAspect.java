package com.kewen.framework.auth.core.annotation.data.authedit;


import com.kewen.framework.auth.core.annotation.AnnotationAuthHandler;
import com.kewen.framework.auth.core.annotation.AuthDataAuthEdit;
import com.kewen.framework.auth.core.exception.AuthException;
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



    @Pointcut("@annotation(com.kewen.framework.auth.core.annotation.AuthDataAuthEdit)")
    public void pointcut(){

    }

    @Around(value = "pointcut() && @annotation(checkDataAuthEdit)",argNames = "joinPoint,checkDataAuthEdit")
    public Object around(ProceedingJoinPoint joinPoint, AuthDataAuthEdit checkDataAuthEdit){
        log.info("编辑业务权限，拦截方法:{}",joinPoint.toString());
        Object[] args = joinPoint.getArgs();
        if (args==null){
            throw new AuthException("参数不能为空");
        }
        Optional<IdDataAuthEdit> first = Arrays.stream(args)
                .filter(a -> a instanceof IdDataAuthEdit)
                .map(a ->(IdDataAuthEdit)a)
                .findFirst();
        if (!first.isPresent()){
            throw new AuthException("参数没有找到接口 IdDataAuthEdit 实现类");
        }
        IdDataAuthEdit dataAuthEdit = first.get();
        try {
            if (checkDataAuthEdit.before()){
                editDataAuths(checkDataAuthEdit, dataAuthEdit);
            }
            Object proceed = joinPoint.proceed();
            if (!checkDataAuthEdit.before()){
                editDataAuths(checkDataAuthEdit, dataAuthEdit);
            }
            return proceed;
        } catch (Throwable throwable) {
            log.error("执行编辑失败"+throwable.getMessage(),throwable);
            throw new RuntimeException(throwable.getMessage(),throwable);
        }
    }

    private void editDataAuths(AuthDataAuthEdit checkDataAuthEdit, IdDataAuthEdit dataAuthEdit) {
        Object dataId = dataAuthEdit.getDataId();
        if (dataId==null){
            throw new RuntimeException("没有数据ID，无法编辑");
        }
        annotationAuthAdaptor.editDataAuths(
                checkDataAuthEdit.businessFunction(),
                dataId,
                checkDataAuthEdit.operate(),
                dataAuthEdit.getAuthObject().listBaseAuth());
    }


    public void setAnnotationAuthAdaptor(AnnotationAuthHandler annotationAuthAdaptor) {
        this.annotationAuthAdaptor = annotationAuthAdaptor;
    }
}
