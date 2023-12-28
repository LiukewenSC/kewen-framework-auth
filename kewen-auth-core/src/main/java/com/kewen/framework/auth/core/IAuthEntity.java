package com.kewen.framework.auth.core;

import com.kewen.framework.auth.core.BaseAuth;

/**
 * 权限实体接口，项目中的基本权限结构体对象，用于转换成BaseAuth通用权限
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
public interface IAuthEntity {

    BaseAuth getAuth();


}
