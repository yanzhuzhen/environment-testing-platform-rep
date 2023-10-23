package Exmpl.Dao;

import Exmpl.Entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface menuMapper extends BaseMapper<Menu> {
    //根据用户ID查询菜单权限

    //根据用户ID查询菜单权限
    List<Menu> findMenuListByUserId(int uno);
}
