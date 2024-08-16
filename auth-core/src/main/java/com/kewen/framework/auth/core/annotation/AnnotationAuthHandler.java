package com.kewen.framework.auth.core.annotation;

import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.model.IAuthObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 *  权限处理器，对于注解涉及到的权限需要的实现都从这里完成
 * <E> 权限对象泛型
 * @author kewen
 * @since 2023-04-10
 */
public interface AnnotationAuthHandler<ID> {

    Logger logger = LoggerFactory.getLogger(AnnotationAuthHandler.class);
    /**
     * 是否有菜单访问权限
     *  对应菜单 范围权限 @AuthCheckMenuAccess
     * @param auths
     * @param path
     * @return
     */
    boolean hasMenuAccessAuth(Collection<BaseAuth> auths, String path) ;
    default boolean hasMenuAccessAuth(IAuthObject authObject, String path) {
        if (authObject==null){
            logger.info("authObject is null");
            return false;
        }
        return hasMenuAccessAuth(authObject.listBaseAuth(), path);
    }

    /**
     * 是否有某条数据的操作权限
     *  对应操作范围权限 @AuthCheckDataOperation
     * @param auths 用户权限
     * @param businessFunction 模块
     * @param operate 操作
     * @param dataId 业务id，如 1L 1011L等业务主键ID
     * @return 是否有权限
     */
    boolean hasDataOperateAuths(Collection<BaseAuth> auths, String businessFunction, ID dataId,String operate);
    default boolean hasDataOperateAuths(IAuthObject authObject, String businessFunction, ID dataId,String operate){
        if (authObject==null){
            logger.info("authObject is null");
            return false;
        }
        return hasDataOperateAuths(authObject.listBaseAuth(), businessFunction, dataId, operate);
    }


    /**
     * 编辑某条 数据权限
     * 但是这里要注意了，不应该编辑此接口本身的权限，否则就会出现自己把自己编辑没，或者把不应该有的人加入（其实就是属于越权了，本应该是上级做的事）
     *  对应编辑数据权限 @AuthEditDataAuth
     * @param dataId 数据ID
     * @param businessFunction 模块
     * @param operate 操作
     * @param auths 权限结构
     */
    void editDataAuths(String businessFunction,ID dataId,  String operate, Collection<BaseAuth> auths);
    default void editDataAuths(String businessFunction,ID dataId,  String operate, IAuthObject authObject){
        if (authObject==null){
            logger.info("authObject is null");
            return;
        }
        editDataAuths(businessFunction, dataId, operate, authObject.listBaseAuth());
    }

    /**
     * 获取数据
     */
    Collection<BaseAuth> getDataAuths(String businessFunction,ID dataId,  String operate);

    /**
     * 填充数据
     */
    default void fillDataAuths(String businessFunction,ID dataId,  String operate,IAuthObject authObject){
        authObject.setProperties(getDataAuths(businessFunction, dataId, operate));
    }

    /**
     * 根据业务功能数据ID删除权限
     * 其实就是清空dataID对应的数据，在业务中删除了此条数据需要调用
     */
    void deleteBusinessFunctionAuthByDataId(String businessFunction,ID dataId);

}
