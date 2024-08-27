package com.kewen.framework.auth.security.extension;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  没考虑明白，rememberMe如果用header传参的话岂不是每个请求都会带上，那就没有意义了，直接把token的时间延长就好了呀
 *  因此token的模式用SpringSession之后这其实也不需要了，
 * @author kewen
 * @since 2024-08-27
 */
@Deprecated
public abstract class AbstractHeaderRememberMeServices implements RememberMeServices, LogoutHandler, InitializingBean {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractHeaderRememberMeServices.class);
    protected String headerName ="remember-me-token";
    protected String rememberMeParam ="remember-me";
    protected String key="123456";
    private String split=",";
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private boolean alwaysRemember;
    protected UserDetailsService userDetailsService;
    protected final MessageSourceAccessor messages = SpringSecurityMessageSource
            .getAccessor();
    //两周
    private int tokenValiditySeconds = 1209600;


    private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public void afterPropertiesSet() {
        Assert.hasLength(key, "key cannot be empty or null");
        Assert.notNull(userDetailsService, "A UserDetailsService is required");
    }

    /**
     * 自动登录
     */
    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(headerName);;
        if (header == null) {
            return null;
        }
        logger.debug("Remember-me header detected");
        UserDetails user = null;

        Pair<String, String> stringStringPair = decodeHeader(header);
        String presentedSeries = stringStringPair.getLeft();
        String presentedToken = stringStringPair.getRight();
        user = processAutoLoginCookie(presentedSeries,presentedToken, request, response);
        userDetailsChecker.check(user);

        logger.debug("Remember-me header accepted");

        return createSuccessfulAuthentication(request, user);
    }
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(key, user,
                authoritiesMapper.mapAuthorities(user.getAuthorities()));
        auth.setDetails(authenticationDetailsSource.buildDetails(request));
        return auth;
    }

    /**
     * 自动登录
     * @param series  解密后的series
     * @param token 解密后的token
     */
    protected abstract UserDetails processAutoLoginCookie(String series,String token,
                                                          HttpServletRequest request, HttpServletResponse response)
            throws RememberMeAuthenticationException, UsernameNotFoundException;


    /**
     * 登录失败
     */
    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {
        //todo 这里要不要考虑清除掉存储
        response.setHeader(headerName,null);
        onLoginFail(request, response);
    }
    protected void onLoginFail(HttpServletRequest request, HttpServletResponse response){
    }

    /**
     * 登录成功的后续处理
     */
    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        if (!rememberMeRequested(request)) {
            logger.debug("Remember-me login not requested.");
            return;
        }

        onLoginSuccess(request, response, successfulAuthentication);
    }
    protected abstract void onLoginSuccess(HttpServletRequest request,
                                           HttpServletResponse response, Authentication successfulAuthentication);

    /**
     * 登出
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setHeader(headerName,null);
    }

    /**
     * 是否支持rememberMe
     */
    protected boolean rememberMeRequested(HttpServletRequest request) {
        if (alwaysRemember) {
            return true;
        }

        String rememberMeStr = request.getHeader(rememberMeParam);
        if (StringUtils.isBlank(rememberMeStr)) {
            rememberMeStr = request.getParameter(rememberMeParam);
        }
        if (rememberMeStr != null) {
            if (rememberMeStr.equalsIgnoreCase("true") || rememberMeStr.equalsIgnoreCase("on")
                    || rememberMeStr.equalsIgnoreCase("yes") || rememberMeStr.equals("1")) {
                return true;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Did not send remember-me header (principal did not set parameter '"
                    + rememberMeParam + "')");
        }

        return false;
    }

    /**
     * 设置返回token
     */
    protected void setResponseToken(PersistentRememberMeToken token, HttpServletRequest request,
                                    HttpServletResponse response) {
        response.setHeader(headerName,encodeHeader(token.getSeries(),token.getTokenValue()));
    }
    private Pair<String,String> decodeHeader(String header) {
        String[] split1 = header.split(split);
        return Pair.of(split1[0], split1[1]);
    }

    private String encodeHeader(String series, String token) {
        String concat = series.concat(split).concat(token);
        //return Base64.getEncoder().encodeToString(concat.getBytes());
        return concat;
    }



    public void setAlwaysRemember(boolean alwaysRemember) {
        this.alwaysRemember = alwaysRemember;
    }

    public void setTokenValiditySeconds(int tokenValiditySeconds) {
        this.tokenValiditySeconds = tokenValiditySeconds;
    }

    protected int getTokenValiditySeconds() {
        return tokenValiditySeconds;
    }

    protected AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
        return authenticationDetailsSource;
    }

    public void setAuthenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource,
                "AuthenticationDetailsSource cannot be null");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    /**
     * Sets the strategy to be used to validate the {@code UserDetails} object obtained
     * for the user when processing a remember-me cookie to automatically log in a user.
     *
     * @param userDetailsChecker the strategy which will be passed the user object to
     * allow it to be rejected if account should not be allowed to authenticate (if it is
     * locked, for example). Defaults to a {@code AccountStatusUserDetailsChecker}
     * instance.
     *
     */
    public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
        this.userDetailsChecker = userDetailsChecker;
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
