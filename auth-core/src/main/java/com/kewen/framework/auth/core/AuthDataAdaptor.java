package com.kewen.framework.auth.core;

import com.kewen.framework.auth.core.data.AuthDataHandler;
import com.kewen.framework.auth.core.entity.IAuthObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  数据权限控制器，使用此服务也可以不依赖 @AuthDataAuthEdit，可以直接在Service层调用
 * @author kewen
 * @since 2024-08-06
 */
public class AuthDataAdaptor {

    @Autowired
    private AuthDataHandler annotationAuthHandler;


    /**
     * 编辑某条 数据权限
     */
    public void editDataAuths(String businessFunction, Object dataId, String operate, IAuthObject authObject){
        if (authObject==null){
            return;
        }
        annotationAuthHandler.editDataAuths(businessFunction, dataId, operate, authObject);
    }

    /**
     * 根据业务功能数据ID删除权限
     * 其实就是清空dataID对应的数据，在业务中删除了此条数据需要调用
     */
    public void deleteBusinessFunctionAuthByDataId(String businessFunction,Object dataId){
        annotationAuthHandler.deleteBusinessFunctionAuthByDataId(businessFunction, dataId);
    }

    /**
     * 填充数据，用于查询某条数据对应的权限集合
     */
    public IAuthObject fillDataAuths(String businessFunction,Object dataId,  String operate, IAuthObject authObject){
        annotationAuthHandler.fillDataAuths(businessFunction, dataId, operate, authObject);
        return authObject;
    }
}
