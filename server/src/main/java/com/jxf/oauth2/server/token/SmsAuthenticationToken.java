package com.jxf.oauth2.server.token;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 重写 AuthenticationToken
 * 参数携带有 短信验证码 表示 未验证
 * 参数携带有 权限信息 表示 通过验证
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private String code;


    public SmsAuthenticationToken(Object principal, String code) {
        super(null);
        this.principal = principal;
        this.code=code;
        setAuthenticated(false);
    }

    public SmsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(false);
    }
}
