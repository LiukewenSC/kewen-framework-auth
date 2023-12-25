package com.kewen.framework.auth.core.annotation.data.edit;


import com.kewen.framework.auth.core.annotation.data.CheckDataOperation;
import com.kewen.framework.auth.core.annotation.AnnotationAuthAdaptor;
import com.kewen.framework.auth.core.context.CurrentUserAuthContext;
import com.kewen.framework.auth.exception.AuthCheckException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * @descrpition 校验业务权限切面，通过业务id和应用校验权限
 * @author kewen
 * @since 2022-11-25
 */
@Component
@Aspect
public class DataCheckAspect {

    private static final Logger log = LoggerFactory.getLogger(DataCheckAspect.class);

    @Autowired
    private AnnotationAuthAdaptor authHandler;



    @Pointcut("@annotation(com.kewen.framework.auth.core.annotation.data.CheckDataOperation)")
    public void pointcut(){}

    @Before(value = "pointcut() && @annotation(authAnn)", argNames = "joinPoint,authAnn")
    public void before(JoinPoint joinPoint, CheckDataOperation authAnn){
        log.info("校验用户权限，拦截方法:{}",joinPoint.toString());
        Object[] args = joinPoint.getArgs();
        if (args==null){
            throw new AuthCheckException("参数不能为空");
        }
        Optional<Object> first = Arrays.stream(args).filter(a -> a instanceof ApplicationBusiness).findFirst();
        if (!first.isPresent()){
            throw new AuthCheckException("参数没有找到接口ApplicationBusiness实现类");
        }
        ApplicationBusiness business = (ApplicationBusiness) first.get();
        Collection<String> userAuths = CurrentUserAuthContext.getAuths();
        boolean hasAuth = authHandler.hasDataOperateAuths(userAuths, authAnn.module(), authAnn.operate(), business.getBusinessId());
        if (!hasAuth){
            throw new AuthCheckException("权限校验不通过");
        }
    }

}
