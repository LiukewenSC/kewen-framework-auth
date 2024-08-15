package com.kewen.framework.auth.core.annotation;

import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.model.IAuthObject;

import java.util.Collection;
/**
 *  数据权限控制器，使用此服务也可以不依赖 @AuthDataAuthEdit，可以直接在Service层调用
 * @author kewen
 * @since 2024-08-06
 */
public class AuthDataAdaptor<ID> {

    private AnnotationAuthHandler<ID> annotationAuthHandler;


    /**
     * 编辑某条 数据权限
     */
    public void editDataAuths(String businessFunction, ID dataId, String operate, IAuthObject authObject){
        annotationAuthHandler.editDataAuths(businessFunction, dataId, operate, authObject.listBaseAuth());
    }

    /**
     * 填充数据，用于查询某条数据对应的权限集合
     */
    public IAuthObject fillDataAuths(String businessFunction,ID dataId,  String operate, IAuthObject authObject){
        Collection<BaseAuth> dataAuths = annotationAuthHandler.getDataAuths(businessFunction, dataId, operate);
        authObject.setProperties(dataAuths);
        return authObject;
    }

    public void setAnnotationAuthHandler(AnnotationAuthHandler<ID> annotationAuthHandler) {
        this.annotationAuthHandler = annotationAuthHandler;
    }
}
