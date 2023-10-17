package Exmpl.Security;

import Exmpl.Entity.Menu;
import Exmpl.Entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import Exmpl.Service.menuService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class myFilterInvocationSecurityMDS implements FilterInvocationSecurityMetadataSource {
   @Autowired
    menuService menuService;
   AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> allMenu = menuService.getAllMenus();
        for (Menu menu : allMenu) {
            if (antPathMatcher.match(menu.getUrl(), requestUrl) && !menu.getRole().isEmpty()) {
                List<Role> roles = menu.getRole();
                int size = roles.size();
                String[] rolename = new String[size];
                for (int i = 0; i < size; i++) {
                    rolename[i] = roles.get(i).getRolename();
                }
                return SecurityConfig.createList(rolename);
            }
        }
            return SecurityConfig.createList("ROLE_LOGIN");

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> Tclass) {
        return FilterInvocation.class.isAssignableFrom(Tclass);
    }
}
