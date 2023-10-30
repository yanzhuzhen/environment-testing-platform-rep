package Exmpl.Service.Inter;

import Exmpl.Entity.Role;
import Exmpl.vo.query.roleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface roleServiceInter extends IService<Role> {
    //查询角色列表
    IPage<Role> findRoleListByUno(IPage<Role> page, roleQueryVo roleQueryVo);

    //检查角色是否被使用
    boolean hasRole(Long id);

    //删除用户角色
    boolean deleteRoleById(Long id);

    boolean saveRoleMenu(Long roleId, List<Long> menuIds);

}
