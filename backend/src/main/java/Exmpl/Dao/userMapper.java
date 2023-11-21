package Exmpl.Dao;
import Exmpl.Entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface userMapper extends BaseMapper<User> {

    @Delete("delete from ur where uno = #{uno};")
    int deleteUserRole(Long uno);

    int saveUserRole(Long uno, List<Long> rnoList);

}
