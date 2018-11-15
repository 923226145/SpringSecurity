package com.lzc.security.config.authentication.social.qq;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class QQAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -8395988993840597793L;

    private final Object principal;

    private Object credentials;

    public QQAuthenticationToken(Object qqToken, Object openId) {
        super((Collection)null);
        this.principal = qqToken;
        this.credentials = openId;
        this.setAuthenticated(false);
    }

    public QQAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
