package Exmpl.Controller;

import Exmpl.Service.redisService;
import Exmpl.Utils.Result;
import Exmpl.Utils.jwtUtils;
import Exmpl.vo.tokenVo;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/sysUser")
public class sysUserController {



    @Resource
    jwtUtils jwtUtils;

    @Resource
    redisService redisService;

    //刷新token
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request){
        //从前端提交中hander或者参数的token
        String token = request.getHeader("token");
        if (ObjectUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //从spring security中获取用户信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //获取用户信息
        String newToken = ""; //定义变量，保存新的token
        if(jwtUtils.checkToken(token,userDetails)){
            newToken = jwtUtils.reToken(token); //生成新token
        }
        //获取到期时间
        long expire = Jwts.parserBuilder()
                .setSigningKey(jwtUtils.getSecret())
                .build()
                .parseClaimsJws(newToken.replace("jwt_", ""))
                .getBody()
                .getExpiration()
                .getTime();
        //在redis中清除原来的token,并存入新的
        String oldTokenKey = "token_"+token;
        redisService.delCache(oldTokenKey);
        String newTokenKey = "token_"+newToken;
        redisService.setCache(newTokenKey,newToken, jwtUtils.getExpires()/1000);
        tokenVo tokenVo = new tokenVo(expire,newToken);
        return Result.ok(tokenVo).message("token刷新成功");
    }
}
