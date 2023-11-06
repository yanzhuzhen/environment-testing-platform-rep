package Exmpl.Security.handler;

import Exmpl.Entity.User;
import Exmpl.Service.redisService;
import Exmpl.Utils.jwtUtils;
import Exmpl.Utils.loginResult;
import Exmpl.Utils.resultCode;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Jwts;
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

    @Resource
    private redisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        User user = (User) authentication.getPrincipal();

        //生成token
        String token = jwtUtils.generateToken(user);

        //设置token的签名密钥和过期时间
        long  expiresTime = Jwts.parserBuilder()
                .setSigningKey(jwtUtils.getSecret())
                .build()
                .parseClaimsJws(token.replace("jwt_", ""))
                .getBody().getExpiration().getTime();

        //创建login带令牌的返回对象
        loginResult loginResult = new loginResult(Math.toIntExact(user.getUno()), resultCode.SUCCESS, token, expiresTime);

        //返回JSON
        String result = JSON.toJSONString(loginResult, SerializerFeature.DisableCircularReferenceDetect);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        //将token信息保存到redis
        String tokenKey = "token_" + token;
        redisService.setCache(tokenKey, token, jwtUtils.getExpires()/1000);
    }
}
