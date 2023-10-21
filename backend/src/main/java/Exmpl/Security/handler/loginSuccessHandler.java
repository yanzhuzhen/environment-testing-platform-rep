package Exmpl.Security.handler;

import Exmpl.Entity.User;
import Exmpl.utils.jwtUtils;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class loginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private jwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        User user = (User) authentication.getPrincipal();
        //生成token
        String token = jwtUtils.genarateToken(user);
        //设置token的签名密钥和过期时间



        String result = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
