package com.Mapper;
import com.Entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Bean;

public interface userMapper extends BaseMapper<User> {
    User findByUsername(@Param("username") String username);
}
