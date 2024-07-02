package com.kewen.framework.auth.rabc.composite;

import com.kewen.framework.auth.rabc.model.UserAuthObject;

public interface SysUserComposite{

    UserAuthObject loadByUsername(String username);

}
