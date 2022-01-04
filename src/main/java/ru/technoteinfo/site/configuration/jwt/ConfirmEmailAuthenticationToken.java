package ru.technoteinfo.site.configuration.jwt;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Transient
public class ConfirmEmailAuthenticationToken extends AbstractAuthenticationToken {

    private Object email;
    private Object authToken;

    public ConfirmEmailAuthenticationToken(Object email, Object authToken) {
        super((Collection)null);
        this.email = email; //principal
        this.authToken = authToken; //credentials
        setAuthenticated(true);
    }

    public ConfirmEmailAuthenticationToken(Object email, Object authToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = email;
        this.authToken = authToken;
        setAuthenticated(true);
    }

    public ConfirmEmailAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getPrincipal() { return email; }

    @Override
    public Object getCredentials() { return authToken; }

}