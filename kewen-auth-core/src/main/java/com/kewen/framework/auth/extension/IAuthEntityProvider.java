package com.kewen.framework.auth.extension;

import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.core.IAuthEntity;

import com.kewen.framework.auth.core.AuthConstant;

/**
 * 简单的权限实体接口，用以约定权限体的 ID、name属性值以及权限标记类型、设置权限值
 * @author kewen
 * @since 2023-12-28
 */
public interface IAuthEntityProvider<ID> extends IAuthEntity {

    /**
     * 权限分隔符，默认为
     * @return
     */
    default String split() {
        return AuthConstant.AUTH_SPLIT;
    }
    /**
     * 获取权限标记类型
     * 如： USER  ROLE  DEPT
     * @return
     */
    String flag();

    /**
     * 根据基础权限设置值
     * @param baseAuth
     */
    void setProperties(BaseAuth baseAuth);
}
