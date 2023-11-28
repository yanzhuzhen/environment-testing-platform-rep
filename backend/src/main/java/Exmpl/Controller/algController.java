package Exmpl.Controller;

import Exmpl.Entity.Alg;
import Exmpl.Entity.Menu;
import Exmpl.Entity.User;
import Exmpl.Service.algService;
import Exmpl.Utils.Result;
import Exmpl.vo.query.algQueryVo;
import Exmpl.vo.query.menuQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/alg")
public class algController {

    @Resource
    algService algService;

    //查询算法菜单
    @GetMapping("/list")
    public Result list(algQueryVo algQueryVo){
        //调用查询菜单列表的方法
        IPage<Alg> page = new Page<Alg>(algQueryVo.getPageNow(), algQueryVo.getPageSize());
        algService.findAlgList(page,algQueryVo);
        return Result.ok(page);

    }

}
