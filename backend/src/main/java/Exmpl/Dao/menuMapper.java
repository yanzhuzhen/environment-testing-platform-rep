package Exmpl.Dao;

import Exmpl.Entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface menuMapper extends BaseMapper<Menu> {
    //根据用户ID查询菜单权限
    List<Menu> findMenuListByUserId( Long uno);

    List<Menu> findMenuListByRoleId(Long rno);

    @Delete("delete from mr where mno = #{mno};")
    int deleteMenuRole(Long mno);
}
