package Exmpl.Security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

@Component
public class myAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication auth, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute CA : configAttributes) {
            String needRole = CA.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)) if (auth instanceof AnonymousAuthenticationToken) {
                throw new BadCredentialsException("您尚未登录，请登陆后访问");
            } else
                return;
            Collection<? extends GrantedAuthority> authorities;
            authorities = auth.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw  new AccessDeniedException("该用户权限不足");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
