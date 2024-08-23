package com.kewen.framework.auth.security.filter.token;

import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kewen
 * @since 2024-05-17
 */
public class TokenSessionHttpServletRequest extends HttpServletRequestWrapper {

    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    private static final ConcurrentHashMap<String, HttpSession> tokenMap = new ConcurrentHashMap<>();

    private final boolean isToken;

    String tokenParameter;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public TokenSessionHttpServletRequest(HttpServletRequest request, String tokenParameter) {
        super(request);
        String token = request.getHeader(tokenParameter);
        tokenThreadLocal.set(token);
        isToken = token != null;
    }

    @Override
    public String getRequestedSessionId() {
        if (!isToken) {
            return super.getRequestedSessionId();
        }
        if (StringUtils.hasText(getToken())) {
            return getToken();
        } else {
            return super.getRequestedSessionId();
        }
    }

    /**
     * 没有token，说明没有从前端传入，可能是登录请求
     *
     * @return
     */
    private String getToken() {
        return tokenThreadLocal.get();
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public Object getAttribute(String name) {
        return super.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object o) {
        super.setAttribute(name, o);
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession innerSession = getInnerSession(create);
        return new TokenSession(innerSession);
    }

    public HttpSession getInnerSession(boolean create) {
        String token = getToken();
        //没有token则生成一个
        if (token == null) {
            HttpSession session = super.getSession(create);
            if (session != null) {
                token = session.getId();
                tokenThreadLocal.set(token);
                tokenMap.put(token, session);
            }
            return session;
        }

        HttpSession session = tokenMap.get(getToken());
        if (session != null) {

            return session;
        }

        //不创建则就直接返回了
        if (!create) {
            return session;
        }
        //创建则创建session，并加入到threadLocal中
        session = super.getSession(true);
        token = session.getId();

        tokenMap.put(token, session);
        tokenThreadLocal.set(token);

        return session;
    }

    static class TokenSession extends StandardSessionFacade {

        public TokenSession(HttpSession session) {
            super(session);
        }

        @Override
        public Object getAttribute(String name) {
            try {
                return super.getAttribute(name);
            } catch (IllegalStateException e) {
                //java.lang.IllegalStateException: getAttribute: Session already invalidated
                throw new CredentialsExpiredException("登录已过期", e);
            }
        }
    }
}
