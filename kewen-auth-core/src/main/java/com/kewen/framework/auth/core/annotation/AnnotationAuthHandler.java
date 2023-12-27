package com.kewen.framework.auth.core.annotation;

import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.annotation.data.range.DataRangeDatabaseField;

/**
 *  权限处理器，对于注解涉及到的权限需要的实现都从这里完成
 * <E> 权限对象泛型
 * @author kewen
 * @since 2023-04-10
 */
public interface AnnotationAuthHandler<A extends IAuthObject,ID> {


    /**
     * 数据范围权限的数据库字段
     * @return
     */
    DataRangeDatabaseField getDataRangeDatabaseField();


    /**
     * 是否有菜单访问权限
     * @param authObject
     * @param url
     * @return
     */
    boolean hasMenuAccessAuth(A authObject, String url) ;









    /**
     * 是否有某条数据的操作权限
     * @param authObject 用户权限
     * @param module 模块
     * @param operate 操作
     * @param businessId 业务id，如 1L 1011L等业务主键ID
     * @return 是否有权限
     */
    boolean hasDataOperateAuths(A authObject, String module, String operate, ID businessId);


    /**
     * 编辑某条 数据权限
     * 但是这里要注意了，不应该编辑此接口本身的权限，否则就会出现自己把自己编辑没，或者把不应该有的人加入（其实就是属于越权了，本应该是上级做的事）
     * @param businessId 业务id
     * @param module 模块
     * @param operate 操作
     * @param authObject 权限结构
     */
    void editDataAuths(ID businessId, String module, String operate, IAuthObject authObject);


}