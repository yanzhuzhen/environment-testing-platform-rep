package Exmpl.Controller;

import Exmpl.Dao.incMapper;
import Exmpl.Dto.roleMenuDTO;
import Exmpl.Entity.Role;
import Exmpl.Service.roleService;
import Exmpl.Service.menuService;
import Exmpl.Utils.Result;
import Exmpl.vo.query.roleQueryVo;
import Exmpl.vo.roleMenuVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static Exmpl.Utils.oparateLogUtils.opalog;

@RestController
@RequestMapping("/api/role")
public class roleController {
    @Resource
    private roleService roleService;
    @Resource
    private menuService menuService;
    @Resource
    private incMapper incMapper;


    //查询角色列表
    @GetMapping("/list")
    public Result list(roleQueryVo roleQueryVo){
        //创建分页对象
        IPage<Role> page = new Page<Role>(roleQueryVo.getPageNow(), roleQueryVo.getPageSize());
        //调用查询方法
        roleService.findRoleListByUno(page, roleQueryVo);
        //返回数据
        return Result.ok(page);
    }

    //添加角色
    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping("/add")
    public Result add(@RequestBody Role role){
        Long id = incMapper.findAllRole() + 1;
        role.setRno(id);
        //调用添加的方法
        if (roleService.save(role)){
            opalog("添加角色");
            return Result.ok().message("角色添加成功");
        }
        return Result.error().message("角色添加失败");
    }

    //修改角色
    @PreAuthorize("hasAuthority('system:role:update')")
    @PutMapping("/update")
    public Result update(@RequestBody Role role){
        //调用修改的方法
        if (roleService.updateById(role)){
            opalog("修改角色");
            return Result.ok().message("角色修改成功");
        }
        return Result.error().message("角色修改失败");
    }

    //删除角色
    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping ("/delete/{id}")
    public Result delete( @PathVariable Long id){
        //调用删除的方法
        if (roleService.deleteRoleById(id)){
            opalog("删除角色");
            return Result.ok().message("角色删除成功");
        }
        return Result.error().message("角色删除失败");
    }

    //判断角色是否被占用
    @PreAuthorize("hasAuthority('system:role:delete')")
    @GetMapping("/check/{id}")
    public Result check(@PathVariable Long id){
        //调用查询角色数量的方法
        if (roleService.hasRole(id)){
            return Result.exist().message("该角色已被使用，不可删除");
        }
        return Result.ok();
    }

    //查询权限树
    @GetMapping("/getAssignMenuTree")
    public Result getAssignMenuTree(Long uno, Long rno){
        roleMenuVo menuTree = menuService.getAssignMenuTree(uno, rno);
        return Result.ok(menuTree);
    }

    //保存权限数据
    @PostMapping("/saveRoleAssign")
    public Result saveRoleAssign(@RequestBody roleMenuDTO roleMenuDTO){
        if(roleService.saveRoleMenu(roleMenuDTO.getRno(), roleMenuDTO.getList())){
            return Result.ok().message("保存成功");
        }else {
            return Result.error().message("保存失败");
        }
    }
}
