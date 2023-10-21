package Exmpl.Security.handler;

import Exmpl.Entity.User;
import Exmpl.utils.resultCode;
import com.alibaba.fastjson.JSON;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountExpiredException;
import Exmpl.utils.Result;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
@Component
public class loginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        String message = null;
        if(exception instanceof BadCredentialsException){
            message = "用户名或密码错误，登录失败";
        }else if (exception instanceof InternalAuthenticationServiceException){
            message = "找不到用户，登录失败";
        }else {
            message = "登录失败";
        }
        //返回json数据
        String result = JSON.toJSONString(Result.error().code(resultCode.ERROR).message(message));
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
