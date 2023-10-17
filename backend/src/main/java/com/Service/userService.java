package com.Service;

import com.Entity.User;
import com.Mapper.userMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class userService implements UserDetailsService {
    @Autowired
    userMapper userMapper;

    public int insertUser (User user){
        return userMapper.insert(user);
    }
    public int updateUser (User user){
        return userMapper.updateById(user);
    }
    public int deleteUser (User user){
        return userMapper.deleteById(user);
    }
    public User selectUser (User user){
        return userMapper.selectById(user.getUno());
    }
    public User selectByUsername(String username){
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
