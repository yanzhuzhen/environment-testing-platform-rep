package Exmpl.Dao;

import Exmpl.Entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface menuMapper extends BaseMapper<Menu> {
    //根据用户ID查询菜单权限
    List<Menu> findMenuListByUserId(@Param("uno") Long uno);

    List<Menu> findMenuListByRoleId(@Param("rno") Long rno);
}
