package Exmpl.Controller;

import Exmpl.Dto.userRoleDTO;
import Exmpl.Dao.incMapper;
import Exmpl.Entity.Role;
import Exmpl.Entity.User;
import Exmpl.Service.roleService;
import Exmpl.Service.userService;
import Exmpl.Utils.Result;
import Exmpl.vo.query.roleQueryVo;
import Exmpl.vo.query.userQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class userController {

    @Resource
    userService userService;
    @Resource
    roleService roleService;
    @Resource
    BCryptPasswordEncoder passwordEncoder;
    @Resource
    incMapper incMapper;


    @Resource
    userRoleDTO userRoleDTO;

    @GetMapping("/list")
    public Result list(userQueryVo userQueryVo) {
        //创建分页对象
        IPage<User> page = new Page<User>(userQueryVo.getPageNow(), userQueryVo.getPageSize());
        userService.findUserListByPage(page,userQueryVo);
        return Result.ok(page);
    }

    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        User u = userService.findUserByUsername(user.getUsername());
        if(u != null){
            return Result.error().message("该用户名已被使用");
        }
        Long id = incMapper.findAllUser() + 1;
        user.setUno(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userService.save(user)){
            return Result.ok().message("用户添加成功");
        }else {
            return Result.error().message("用户添加失败");
        }
    }

    @PreAuthorize("hasAuthority('system:user:update')")
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        //查询用户
        User i = userService.findUserByUsername(user.getUsername());
        if(i != null && i.getUno() != user.getUno()){
            return Result.error().message("该用户名已被使用");
        }
        if(userService.updateById(user)){
            return Result.ok().message("用户修改成功");
        }else {
            return Result.error().message("用户修改失败");
        }
    }

    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if(userService.deleteUserById(id)){
            return Result.ok().message("用户删除成功");
        }else {
            return Result.error().message("用户删除失败");
        }
    }

    //获取分配的角色列表,该用户创建过什么角色，即他可以分配的权限
    @GetMapping("/getRoleListForAssign")
    public Result getRoleListForAssign(roleQueryVo roleQueryVo){
        IPage<Role> page = new Page<Role>(roleQueryVo.getPageNow(), roleQueryVo.getPageSize());
        roleService.findRoleListByUno(page,roleQueryVo);
        return Result.ok(page);
    }

    //根据用户ID查询该用户的拥有的角色列表
    @GetMapping("/getRnoByUno/{id}")
    public Result getRnoByUno(@PathVariable Long id){
        List<Long> rnoList = roleService.findRnoByUno(id);
        return Result.ok(rnoList);
    }

    @PostMapping("/saveUserRole")
    public Result saveUserRole(@RequestBody userRoleDTO userRoleDTO){
        if (userService.saveUserRole(userRoleDTO.getUno(), userRoleDTO.getRnoList())){
            return Result.ok().message("角色分配成功");
        }
            return Result.error().message("角色分配失败");
    }

    @DeleteMapping("/deleteRole/{id}")
    public Result deleteRole(@PathVariable Long id){

        if(userService.deleteRole(id)){
            return Result.ok().message("角色删除成功");
        }else {
            return Result.error().message("角色删除失败");
        }
    }

    @GetMapping("/getAvatar/{id}")
    public Result getAvatar(@PathVariable Long id){
        String avatar = userService.findAvatarByUno(id);
        return Result.ok(avatar);
    }

    @GetMapping("/getRolename/{id}")
    public Result getRolename(@PathVariable Long id){
        List<String> rolenameList = roleService.findRolenameByUno(id);
        return Result.ok(rolenameList);
    }

    @PutMapping("/edit")
    public Result edit(@RequestBody User user){
        //查询用户
        User i = userService.findUserByUsername(user.getUsername());
        if(i != null && i.getUno() != user.getUno()){
            return Result.error().message("该用户名已被使用");
        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userService.updateById(user)){
            return Result.ok().message("个人信息修改成功");
        }else {
            return Result.error().message("个人信息修改失败");
        }
    }

}
