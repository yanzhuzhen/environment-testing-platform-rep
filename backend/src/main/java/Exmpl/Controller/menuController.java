package Exmpl.Controller;

import Exmpl.Entity.Menu;
import Exmpl.Utils.Result;
import Exmpl.vo.query.menuQueryVo;
import org.springframework.web.bind.annotation.*;
import Exmpl.Service.menuService;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class menuController {
    @Resource
    private menuService menuService;

    //查询菜单列表
    @GetMapping("/list")
    public Result list(menuQueryVo menuQueryVo){
        //调用查询菜单列表的方法
        List<Menu> menuList = menuService.findMenuList(menuQueryVo);
        //返回数据
        return Result.ok(menuList);
    }

    //查询上级菜单列表
    @GetMapping("/parent/list")
    public Result parentList(menuQueryVo menuQueryVo){
        //调用查询菜单列表的方法
        List<Menu> menuList = menuService.findMenuParent();
        //返回数据
        return Result.ok(menuList);
    }

    //添加菜单
    @PostMapping("/add")
    public Result add(@RequestBody Menu menu){
        //调用新增的方法
        if (menuService.save(menu)){
            return Result.ok().message("菜单添加成功");
        }
        return Result.error().message("菜单添加失败");
    }

    //修改菜单
    @PutMapping("/update")
    public Result update(@RequestBody Menu menu){
        //调用修改的方法
        if (menuService.updateById(menu)){
            return Result.ok().message("菜单修改成功");
        }
        return Result.error().message("菜单修改失败");
    }

    //删除菜单
    @DeleteMapping ("/delete/{id}")
    public Result delete( @PathVariable Long id){
        //调用删除的方法
        if (menuService.removeById(id)){
            return Result.ok().message("菜单删除成功");
        }
        return Result.error().message("菜单删除失败");
    }

    //检查是否有子菜单
    @GetMapping("/check/{id}")
    public Result check(@PathVariable Long id){
        //判断菜单是否有子菜单
        if(menuService.hasChildOfMenu(id)){
            return Result.exist().message("该菜单下有子菜单，无法删除！！");
        }
        return Result.ok().message("该菜单没有子菜单，可以删除");

    }


}
