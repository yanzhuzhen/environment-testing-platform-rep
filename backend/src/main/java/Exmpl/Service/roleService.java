package Exmpl.Service;

import Exmpl.Dao.userMapper;
import Exmpl.Entity.Role;

import Exmpl.Dao.roleMapper;
import Exmpl.Entity.User;
import Exmpl.Service.Inter.roleServiceInter;
import Exmpl.vo.query.roleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class roleService extends ServiceImpl<roleMapper, Role> implements roleServiceInter {
    @Resource
    roleMapper roleMapper;
    @Resource
    userMapper userMapper;

    public int insertRole (Role role){
        return roleMapper.insert(role);
    }
    public int updateRole (Role role){
        return roleMapper.updateById(role);
    }
    public int deleteRole (Role role){
        return roleMapper.deleteById(role);
    }
    public Role selectRole (Role role){
        return roleMapper.selectById(role.getRno());
    }

    @Override
    public IPage<Role> findRoleListByUno(IPage<Role> page, roleQueryVo roleQueryVo) {
        //根据用户ID查询用户信息
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        queryWrapper.like(!ObjectUtils.isEmpty(roleQueryVo.getRolename()),"rolename",roleQueryVo.getRolename());
        queryWrapper.orderByAsc("rno");
        User user = userMapper.selectById(roleQueryVo.getUno());
        if(user != null && !ObjectUtils.isEmpty(user.getIsadmin()) && user.getIsadmin()==0 ){
            //非管理，只能查询自己的角色
            queryWrapper.eq("create_user", roleQueryVo.getUno());
        }
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean hasRole(Long id) {
        return baseMapper.getRoleCountByRno(id)>0;
    }

    @Override
    public boolean deleteRoleById(Long id) {
        //删除角色权限关系
        baseMapper.deleteRoleMenuByRno(id);
        //删除角色
        return baseMapper.deleteById(id)>0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean saveRoleMenu(Long rno, List<Long> menuIds) {
        baseMapper.deleteRoleMenuByRno(rno);
        return baseMapper.saveRoleMenu(rno,menuIds) > 0;
    }

    @Override
    public List<Long> findRnoByUno(Long id) {
        return baseMapper.findRnoByUno(id);
    }
}
