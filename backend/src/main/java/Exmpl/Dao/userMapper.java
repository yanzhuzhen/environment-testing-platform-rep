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

    @Update("update usertable set is_enabled = 1 where email = #{email}")
    int isEnable(String email);

    int saveUserRole(Long uno, List<Long> rnoList);

    User getUserByEmail(String email);

    @Insert("insert into ur(uno,rno) values(#{uno},3)")
    void setUR(Long uno);

}
