package com.kewen.framework.auth.sys.composite;

import com.kewen.framework.auth.sys.model.UserAuthObject;

public interface SysUserComposite{

    UserAuthObject login(String username, String password);

}
