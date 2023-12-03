package Exmpl.Controller;

import Exmpl.Dao.incMapper;
import Exmpl.Dao.algMapper;
import Exmpl.Entity.Alg;
import Exmpl.Service.algService;
import Exmpl.Utils.Result;
import Exmpl.vo.query.algQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/api/alg")
public class algController {

    @Resource
    algService algService;

    @Resource
    incMapper incMapper;

    @Resource
    algMapper algMapper;

    //查询算法菜单
    @GetMapping("/list")
    public Result list(algQueryVo algQueryVo){
        IPage<Alg> page = new Page<Alg>(algQueryVo.getPageNow(), algQueryVo.getPageSize());
        algService.findAlgList(page,algQueryVo);
        return Result.ok(page);

    }

    @PostMapping("/add")
    public Result add(@RequestBody Alg alg){
        Alg a = algService.findAlgByAlgname(alg.getAlgname());
        if(a != null){
            return Result.error().message("该算法名已被使用");
        }
        Long id = incMapper.findAllAlg() + 1;
        alg.setAno(id);
        if(algService.save(alg)){
            return Result.ok().message("算法添加成功");
        }else {
            return Result.error().message("算法添加失败");
        }
    }


    @PutMapping("/update")
    public Result update(@RequestBody Alg alg){
        //查询用户
        Alg a = algService.findAlgByAlgname(alg.getAlgname());
        if(a != null && !Objects.equals(a.getAno(), alg.getAno())){
            return Result.error().message("该算法名已被使用");
        }
        if(algService.updateById(alg)){
            return Result.ok().message("算法修改成功");
        }else {
            return Result.error().message("算法修改失败");
        }
    }


    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if(algService.deleteAlgById(id)){
            return Result.ok().message("算法删除成功");
        }else {
            return Result.error().message("算法删除失败");
        }
    }

    @GetMapping("/alg/{id}")
    public Result context(@PathVariable Long id){
        Alg alg = algService.findAlgByAno(id);
        return Result.ok(alg);
    }

    @PutMapping("/push/{id}")
    public Result push(@PathVariable Long id){
        if(algMapper.ispermit(id)>0){
            return Result.ok().message("审核成功！！");
        }
        return Result.error().message("审核失败！！");
    }
}
