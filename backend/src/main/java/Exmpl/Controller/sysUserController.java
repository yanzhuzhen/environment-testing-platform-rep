package Exmpl.Controller;

import Exmpl.Dto.userDTO;
import Exmpl.Entity.Menu;
import Exmpl.Entity.User;
import Exmpl.Entity.userInfo;
import Exmpl.Service.redisService;
import Exmpl.Service.userService;
import Exmpl.Utils.Result;
import Exmpl.Utils.jwtUtils;
import Exmpl.vo.routerVo;
import Exmpl.vo.tokenVo;
import ch.qos.logback.classic.Logger;
import io.jsonwebtoken.Jwts;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import Exmpl.Utils.menuTree;

import static Exmpl.Utils.oparateLogUtils.opalog;
import static Exmpl.Utils.systemLogUtils.syslog;

@Api(tags = "用户信息管理")
@Slf4j
@RestController
@RequestMapping("/api/sysUser")
public class   sysUserController {
    @Resource
    jwtUtils jwtUtils;

    @Resource
    redisService redisService;

    @Resource
    userService userService;

    @Resource
    BCryptPasswordEncoder passwordEncoder;

    //获取用户信息
    @GetMapping("/getInfo")
    public Result getInfo(){
        //从spring security中获取用户信息,并判断是否为空
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return Result.error().message("用户信息查询失败");
        }

        User user = (User) authentication.getPrincipal(); //获取用户信息
        List<Menu> menuList = user.getMenuList(); //获取授权信息
        Object[] roles = menuList.stream().filter(Objects::nonNull).map(Menu::getCode).toArray(); //获取权限编码
        userInfo userInfo = new userInfo(user.getUno(), user.getUsername(), user.getRealname(), user.getPhone(), user.getEmail(), user.getAvatar(), null, roles);
        syslog("查询用户信息成功");
        return Result.ok(userInfo).message("用户信息查询成功");

    }

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
        syslog("token刷新");
        return Result.ok(tokenVo).message("token刷新成功");
    }

    //获取菜单数据
    @GetMapping("/getMenuList")
    public Result getMenuList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //从spring security中获取用户信息
        User user = (User) authentication.getPrincipal(); //获取用户信息
        List<Menu> menuList = user.getMenuList(); //获取相应权限
        //筛选目录和菜单
        List<Menu> menuCollect = menuList.stream()
                .filter(item -> item != null && item.getType() != 2) //2为按钮，菜单树中不展示按钮，0和1为目录和菜单
                .collect(Collectors.toList());

        List<routerVo> routerVoList = menuTree.makeRouter(menuCollect, 0L); //生成路由数据
        syslog("获取菜单数据");
        return Result.ok(routerVoList).message("获取菜单数据成功");


    }

    //退出登录
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response){
        //从前端提交中hander或者参数的token
        String token = request.getHeader("token");
        if (ObjectUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //从spring security中获取用户信息
        //判断用户信息是否为空，不为空则清空
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            User user = (User) authentication.getPrincipal();
            //清除redis缓存中的
            redisService.delCache("token_"+token);
            return Result.ok().message("退出登录成功");
        }
        return Result.error().message("退出登录失败");
    }
    // 发送邮件
    @PostMapping("/sendCode")
    public Result registerByEmail(@RequestBody userDTO userDTO){
        try {
            log.info(userDTO.getUsername()+"::"+"获取验证码");
            return userService.registerEmail(userDTO);
        } catch (Exception e) {
            opalog(userDTO.getUsername()+"::"+"获取验证码失败");
            return Result.error().message("获取验证码失败");
        }
    }
    // 通过邮箱注册
    @PostMapping("/signup")
    public Result activation(@RequestBody HashMap<String,String> para){
        try {
            String email=para.get("email");
            String code = para.get("code");
            return userService.activation(email,code);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message("注册失败");
        }
    }

    // 发送邮件
    @PostMapping("/updatePasswordByEmail")
    public Result updatePasswordByEmail(@RequestBody userDTO userDTO){
        try {
            log.info(userDTO.getUsername()+"::"+"获取验证码");
            return userService.updatePasswordByEmail(userDTO);
        } catch (Exception e) {
            log.info(userDTO.getUsername()+"::"+"获取验证码失败");
            return Result.error().message("获取验证码失败");
        }
    }

    // 通过邮箱修改密码
    @PostMapping("/activationPassword")
    public Result activationPassword(@RequestBody HashMap<String,String> para){
        try {
            String email=para.get("email");
            String code = para.get("code");
            return userService.activationPassword(email,code);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message("修改密码失败，请重试");
        }
    }


}
