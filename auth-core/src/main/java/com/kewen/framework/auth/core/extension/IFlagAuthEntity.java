package com.kewen.framework.auth.core.extension;

import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.model.IAuthEntity;

import com.kewen.framework.auth.core.model.AuthConstant;

/**
 * 简单的权限实体接口，用以约定权限体的 ID、name属性值以及权限标记类型、设置权限值
 * @author kewen
 * @since 2023-12-28
 */
public interface IFlagAuthEntity<ID> extends IAuthEntity {

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
