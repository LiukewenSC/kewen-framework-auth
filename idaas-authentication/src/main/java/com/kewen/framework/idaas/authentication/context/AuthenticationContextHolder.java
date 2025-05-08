package com.kewen.framework.idaas.authentication.context;


/**
 * 2025/04/13
 *
 * @author kewen
 * @since 1.0.0
 */
public class AuthenticationContextHolder {

    public static final ThreadLocal<AuthenticationContext> CONTEXT = new InheritableThreadLocal<>();

    public static void setUserContext(AuthenticationContext authenticationContext) {
        CONTEXT.set(authenticationContext);
    }
    public static AuthenticationContext getUserContext() {
        return CONTEXT.get();
    }
}
