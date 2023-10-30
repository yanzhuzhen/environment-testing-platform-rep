package Exmpl.Dao;

import Exmpl.Entity.Role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;


public interface roleMapper extends BaseMapper<Role> {

    //查询角色数量
    @Select("select count(1) from role where rno = #{id}")
    int getRoleCountByRno(Long id);

    //删除角色权限关系
    @Delete("delete from MR where rno = #{id}")
    void deleteRoleMenuByRno(Long id);
}
