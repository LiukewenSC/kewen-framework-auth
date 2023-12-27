package com.kewen.framework.auth.core.annotation.data.authedit;


import com.kewen.framework.auth.core.annotation.AnnotationAuthAdaptor;
import com.kewen.framework.auth.core.annotation.data.EditDataAuth;
import com.kewen.framework.auth.exception.AuthCheckException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author kewen
 * @descrpition 编辑权限切面
 * @since 2022-12-19
 */
@Component
@Aspect
public class AuthDataEditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuthDataEditAspect.class);

    @Autowired
    private AnnotationAuthAdaptor annotationAuthAdaptor;



    @Pointcut("@annotation(com.kewen.framework.auth.core.annotation.data.EditDataAuth)")
    public void pointcut(){

    }

    @Around(value = "pointcut() && @annotation(checkDataAuthEdit)",argNames = "joinPoint,checkDataAuthEdit")
    public Object around(ProceedingJoinPoint joinPoint, EditDataAuth checkDataAuthEdit){
        log.info("编辑业务权限，拦截方法:{}",joinPoint.toString());
        Object[] args = joinPoint.getArgs();
        if (args==null){
            throw new AuthCheckException("参数不能为空");
        }
        Optional<AuthDataEditBusiness> first = Arrays.stream(args)
                .filter(a -> a instanceof AuthDataEditBusiness)
                .map(a ->(AuthDataEditBusiness)a)
                .findFirst();
        if (!first.isPresent()){
            throw new AuthCheckException("参数没有找到接口 AuthDataEditBusiness 实现类");
        }
        AuthDataEditBusiness authDataEditBusiness = first.get();

        annotationAuthAdaptor.editDataAuths(
                authDataEditBusiness.getBusinessId(),
                checkDataAuthEdit.module(),
                checkDataAuthEdit.operate(),
                authDataEditBusiness.getAuthObject());

        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("执行编辑失败"+throwable.getMessage(),throwable);
            throw new RuntimeException(throwable.getMessage(),throwable);
        }
    }

}
