package com.kewen.framework.auth.sys.model;

import com.kewen.framework.auth.support.SimpleAuthObject;
import com.kewen.framework.auth.sys.mp.entity.SysUser;
import lombok.Data;

/**
 * @author kewen
 * @since 2024-05-11
 */
@Data
public class UserAuthObject{
    private SysUser sysUser;
    private SimpleAuthObject authObject;
}
