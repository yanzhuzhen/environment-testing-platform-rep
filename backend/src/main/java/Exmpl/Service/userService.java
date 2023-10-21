package Exmpl.Service;

import Exmpl.Entity.User;
import Exmpl.Mapper.userMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service

public class userService{
    @Autowired
    userMapper userMapper;

    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    public int updateUser(User user) {
        return userMapper.updateById(user);
    }

    public int deleteUser(User user) {
        return userMapper.deleteById(user);
    }

    public User selectUser(User user) {
        return userMapper.selectById(user.getUno());
    }

    public User selectByUsername(String username) {
        return userMapper.findByUsername(username);
    }


}


