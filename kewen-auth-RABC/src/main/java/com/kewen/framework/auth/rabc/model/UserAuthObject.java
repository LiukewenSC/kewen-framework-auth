package com.kewen.framework.auth.rabc.model;

import com.kewen.framework.auth.rabc.composite.model.SimpleAuthObject;
import com.kewen.framework.auth.rabc.mp.entity.SysUser;
import com.kewen.framework.auth.rabc.mp.entity.SysUserCredential;
import lombok.Data;

/**
 * @author kewen
 * @since 2024-05-11
 */
@Data
public class UserAuthObject{
    private SysUser sysUser;
    private SysUserCredential sysUserCredential;
    private SimpleAuthObject authObject;
}
