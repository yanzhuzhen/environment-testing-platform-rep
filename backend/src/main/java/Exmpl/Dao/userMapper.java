package Exmpl.Dao;
import Exmpl.Entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface userMapper extends BaseMapper<User> {

    int saveUser(User user);

    @Delete("delete from ur where uno = #{uno};")
    int deleteUserRole(Long uno);

    @Select("select count(1) from usertable where email = #{email}")
    int findUserByEmail(String email);

    @Select("select username from usertable where uno = #{id}")
    String findUserById(Long id);

    @Update("update usertable set is_enabled = 1 where email = #{email}")
    void isEnable(String email);

    @Update("update usertable set is_enabled = 0 where email = #{email}")
    void isNonEnable(String email);

    @Update("update usertable set password = #{password} where email = #{email}")
    void updatePassword(String password, String email);

    @Update("update usertable set email = #{newemail} where email = #{email}")
    void updateEmail(String newemail, String email);

    int saveUserRole(Long uno, List<Long> rnoList);

    User getUserByEmail(String email);

    @Select("select avatar from usertable where uno = #{uno}")
    String findAvatarByUno(Long uno);

}
