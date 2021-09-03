package ru.technoteinfo.site.configuration.jwt.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.technoteinfo.site.configuration.jwt.ConfirmEmailAuthenticationToken;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.services.TechnoUserDetailService;

@Component
public class EmailTokenAuthenticationProvider implements AuthenticationProvider {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    @Autowired
    private TechnoUserDetailService technoUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String email = authentication.getPrincipal().toString();
        String authToken = authentication.getCredentials().toString();

        TechnoUserDetail user;
        try {
            user = (TechnoUserDetail) this.technoUserDetailsService.loadUserByUsername(email);
            if (user == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            }
        } catch (UsernameNotFoundException var4) {
            throw var4;
        } catch (InternalAuthenticationServiceException var5) {
            throw var5;
        } catch (Exception var6) {
            throw new InternalAuthenticationServiceException(var6.getMessage(), var6);
        }
        Object principalToReturn = user;

        ConfirmEmailAuthenticationToken token =  new ConfirmEmailAuthenticationToken(email, authToken);
        return this.createSuccessAuthentication(principalToReturn, authentication, user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(ConfirmEmailAuthenticationToken.class);
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        ConfirmEmailAuthenticationToken result = new ConfirmEmailAuthenticationToken(principal, authentication.getCredentials(), this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        this.logger.debug("Авторизован подтвежденный пользователь");
        return result;
    }
}
