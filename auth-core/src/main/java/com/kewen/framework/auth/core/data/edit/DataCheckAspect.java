package com.kewen.framework.auth.core.data.edit;


import com.kewen.framework.auth.core.entity.CurrentUser;
import com.kewen.framework.auth.core.AuthDataOperation;
import com.kewen.framework.auth.core.data.AuthDataHandler;
import com.kewen.framework.auth.core.AuthUserContext;
import com.kewen.framework.auth.core.exception.AuthException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * @descrpition 校验业务权限切面，通过业务id和应用校验权限
 * @author kewen
 * @since 2022-11-25
 */
@Aspect
public class DataCheckAspect {

    private static final Logger log = LoggerFactory.getLogger(DataCheckAspect.class);

    private AuthDataHandler annotationAuthHandler;



    @Pointcut("@annotation(com.kewen.framework.auth.core.AuthDataOperation)")
    public void pointcut(){}

    @Before(value = "pointcut() && @annotation(authAnn)", argNames = "joinPoint,authAnn")
    public void before(JoinPoint joinPoint, AuthDataOperation authAnn){
        log.info("校验用户权限，拦截方法:{}",joinPoint.toString());
        Object[] args = joinPoint.getArgs();
        if (args==null){
            throw new AuthException("参数不能为空");
        }
        Optional<Object> first = Arrays.stream(args).filter(a -> a instanceof IdDataEdit).findFirst();
        if (!first.isPresent()){
            throw new AuthException("参数没有找到接口 IdDataEdit 实现类");
        }
        IdDataEdit business = (IdDataEdit) first.get();
        CurrentUser currentUser = AuthUserContext.getCurrentUser();
        boolean hasAuth = annotationAuthHandler.hasDataOperateAuths(currentUser.getAuthObject(), authAnn.businessFunction(), business.getDataId(),authAnn.operate());
        if (!hasAuth){
            throw new AuthException("权限校验不通过");
        }
    }

    public void setAnnotationAuthHandler(AuthDataHandler annotationAuthHandler) {
        this.annotationAuthHandler = annotationAuthHandler;
    }
}
