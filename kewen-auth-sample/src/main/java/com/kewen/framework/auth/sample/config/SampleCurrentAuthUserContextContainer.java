package com.kewen.framework.auth.sample.config;

import com.kewen.framework.auth.core.context.CurrentAuthUserContextContainer;
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
    public Collection<String> getAuths() {
        return Arrays.asList("R_1","U_1");
    }

    @Override
    public void setAuths(Collection<String> auths) {

    }
}
