package Exmpl.Security;


import Exmpl.Security.handler.loginFailHandler;
import Exmpl.Security.handler.loginSuccessHandler;
import Exmpl.Security.handler.anonymousAuthenticationHandler;
import Exmpl.Security.handler.userAccessDeniedHandler;

import Exmpl.Service.userDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private loginSuccessHandler loginSuccessHandler;
    @Resource
    private loginFailHandler loginFailHandler;
    @Resource
    private anonymousAuthenticationHandler anonymousAuthenticationHandler;
    @Resource
    private userAccessDeniedHandler userAccessDeniedHandler;
    @Resource
    private userDetailsService userDetailsService;
    //注入加密类
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //处理登录认证
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录过程的处理
        http.formLogin() //表单登录
                .loginProcessingUrl("/user/login") //登录请求的url
                .successHandler(loginSuccessHandler) //登陆成功处理器
                .failureHandler(loginFailHandler) //登陆失败处理器
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //不创建Session
                .and()
                .authorizeRequests() //需要拦截的请求
                .antMatchers("/api/login").permitAll() //登陆请求放行
                .anyRequest().authenticated()  //其他请求都不放行
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(anonymousAuthenticationHandler) //匿名用户无权限访问
                .accessDeniedHandler(userAccessDeniedHandler) //认证用户无权限访问
                .and()
                .cors(); //支持跨域


    }

    //配置认证处理器
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}
