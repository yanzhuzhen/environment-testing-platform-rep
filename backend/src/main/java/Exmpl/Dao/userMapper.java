package Exmpl.Dao;
import Exmpl.Entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface userMapper extends BaseMapper<User> {
    User findByUsername(@Param("username") String username);
}
