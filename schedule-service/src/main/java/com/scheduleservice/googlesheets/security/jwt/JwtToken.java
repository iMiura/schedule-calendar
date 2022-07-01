package com.scheduleservice.googlesheets.security.jwt;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * jwt tokenをshiroで認定処理
 *
 * @author :keisho
 */
@Data
public class JwtToken implements AuthenticationToken, RememberMeAuthenticationToken {

    private static final long serialVersionUID = -5798103949631777137L;
    private String token;
    private Boolean rememberMe;

    public JwtToken(String token) {
        this.token = token;
        this.rememberMe = false;
    }

    public JwtToken(String token, Boolean rememberMe) {
        this.token = token;
        this.rememberMe = rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return getCredentials();
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }
}

