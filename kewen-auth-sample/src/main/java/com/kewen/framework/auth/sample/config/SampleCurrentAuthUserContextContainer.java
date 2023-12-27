package com.kewen.framework.auth.sample.config;

import com.kewen.framework.auth.core.IAuthObject;
import com.kewen.framework.auth.core.context.CurrentAuthUserContextContainer;
import com.kewen.framework.auth.core.BaseAuth;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */
@Component
public class SampleCurrentAuthUserContextContainer implements CurrentAuthUserContextContainer {

    @Override
    public Collection<BaseAuth> getAuths() {
        return Arrays.asList(new BaseAuth("R_1","R_超级管理员"),new BaseAuth("U_1","U_张三"));
    }

    @Override
    public void setAuths(Collection<String> auths) {

    }

    @Override
    public IAuthObject getAuthObject() {
        // todo
        return null;
    }
}
