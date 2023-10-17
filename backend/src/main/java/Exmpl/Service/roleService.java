package Exmpl.Service;

import Exmpl.Entity.Role;

import Exmpl.Mapper.roleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class roleService {
    @Autowired
    roleMapper roleMapper;

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
}
