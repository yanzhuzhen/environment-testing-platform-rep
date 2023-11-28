package Exmpl.Dao;

import Exmpl.Entity.Role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface roleMapper extends BaseMapper<Role> {

    //查询角色数量
    @Select("select count(1) from roletable where rno = #{id}")
    int getRoleCountByRno(Long id);

    //删除角色权限关系
    @Delete("delete from MR where rno = #{id}")
    void deleteRoleMenuByRno(Long id);

    //保存角色权限关系
    int saveRoleMenu(Long rno, List<Long> menuIds);

    @Select("select rno from ur where uno = #{id}")
    List<Long> findRnoByUno(Long id);

    @Select("select rolename from roletable, ur where roletable.rno = ur.rno and uno = #{id}")
    List<String> findRolenameByUno(Long id);
}
