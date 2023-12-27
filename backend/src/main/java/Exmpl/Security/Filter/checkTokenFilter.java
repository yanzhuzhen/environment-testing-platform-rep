package Exmpl.Security.Filter;

import Exmpl.Service.redisService;
import Exmpl.Service.userDetailsService;
import Exmpl.Utils.Exception.userAuthenticationException;
import Exmpl.Utils.jwtUtils;
import lombok.Data;

import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.management.remote.JMXAuthenticator;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import Exmpl.Security.handler.loginFailHandler;

@Component
@EqualsAndHashCode(callSuper = true)
@Data
public class checkTokenFilter extends OncePerRequestFilter {


    private  String loginUrl = "/api/user/login";
    private  String signupUrl = "/api/sysUser/signup";
    private  String sendCodeUrl = "/api/sysUser/sendCode";
    private  String swaggerUrl ="/swagger-ui.html";
    private  String swaggerUrll ="/swagger**/**";
    private  String swaggerUrlll ="/webjars/**";
    private  String swaggerUrllll ="/v2/**";




    @Resource
    private redisService redisService;

    @Resource
    private jwtUtils jwtUtils;

    @Resource
    private userDetailsService userDetailsService;

    @Resource
    private loginFailHandler loginFailHandler;


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String url = request.getRequestURI();  //获取当前请求的URL
            System.out.println(url);
            //判断当前请求是否为登录，如不是则验证Token
            if(!url.equals(loginUrl) && !url.equals(signupUrl) && !url.equals(sendCodeUrl)&& !url.equals(swaggerUrl)&& !url.equals(swaggerUrll)&& !url.equals(swaggerUrlll)&& !url.equals(swaggerUrllll)){
                System.out.println("1111");
                this.checkToken(request);

            }
        }catch (AuthenticationException e){
             loginFailHandler.onAuthenticationFailure(request, response, e );
//            System.out.println("1111");
        }
//        System.out.println("1111");
        doFilter(request, response, filterChain); // 登录请求不用验证


    }

    //验证Token
    private void checkToken(HttpServletRequest request) {
        //获取前端提交的token信息，从头部和参数获取
        String token = request.getHeader("token");
        if(ObjectUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        //都没有则抛异常
        if (ObjectUtils.isEmpty(token)){
            throw new userAuthenticationException("token为空");
        }
        //判断redis中有没有token，如果为空则已失效
        String tokenKey = "token_"+token;
        String redisToken = redisService.getCache(tokenKey);
        if(ObjectUtils.isEmpty(redisToken)){
            throw new userAuthenticationException("token已过期");
        }
        //如果生成的token和redis中的token不一致，则失败
        if(!token.equals(redisToken)){
            throw new userAuthenticationException("token验证失败");
        }
        //存在,则解析数据
        String username = jwtUtils.getUsernameFromToken(token);
        if (ObjectUtils.isEmpty(username)){
            throw new userAuthenticationException("token解析失败");
        }
        //获取用户信息并判断
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails == null){
            throw new userAuthenticationException("token验证失败");
        }
        //创建用户认证对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        //设置请求信息
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //把认证信息给SpringSecurity
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);// 构造用户名密码认证信息



    }


}
