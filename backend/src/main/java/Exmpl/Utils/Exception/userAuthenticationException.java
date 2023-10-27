package Exmpl.Utils.Exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class userAuthenticationException extends AuthenticationServiceException {
    public userAuthenticationException(String m) {
        super(m);
    }
}
