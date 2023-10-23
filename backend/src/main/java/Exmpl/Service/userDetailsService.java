package Exmpl.Service;

import Exmpl.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class userDetailsService implements UserDetailsService {
    @Autowired
    Exmpl.Dao.userMapper userMapper;

    public User selectByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        return user;
    }
}
