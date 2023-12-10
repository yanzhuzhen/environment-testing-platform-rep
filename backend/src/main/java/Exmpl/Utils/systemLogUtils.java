package Exmpl.Utils;

import Exmpl.Entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class systemLogUtils {
    public static void syslog(String m){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); //获取用户信息
        log.info(user.getUsername()+"::"+m);
    }
}
