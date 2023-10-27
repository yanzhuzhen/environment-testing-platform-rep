package Exmpl.Service;

import Exmpl.Entity.Menu;
import Exmpl.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class userDetailsService implements UserDetailsService {
    @Resource
    Exmpl.Dao.userMapper userMapper;
    @Resource
    private menuService menuService;


    public User selectByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //查询当前登录用户拥有的权限列表
        List<Menu> menuList = menuService.findMenuListByUserId(user.getUno());
        //获取对应权限编码
        List<String> codeList = menuList.stream().filter(Objects::nonNull)
                .map(Menu::getCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        //获取授权
        String[] S = codeList.toArray(new String[codeList.size()]);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(S);
        user.setAuthorities(authorities);
        user.setMenuList(menuList);
        return user;
    }
}
