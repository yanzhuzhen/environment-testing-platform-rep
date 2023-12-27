package Exmpl.Controller;

import Exmpl.Entity.Log;
import Exmpl.Service.logService;
import Exmpl.Utils.Result;
import Exmpl.vo.query.logQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static Exmpl.Utils.oparateLogUtils.opalog;

@Api(tags = "日志管理")
@RestController
@RequestMapping("/api/log")
public class logController {

    @Resource
    logService logService;
    
    @GetMapping("/list")
    public Result list(logQueryVo logQueryVo) {
        //创建分页对象
        IPage<Log> page = new Page<Log>(logQueryVo.getPageNow(), logQueryVo.getPageSize());
        logService.findlogListByPage(page,logQueryVo);
        return Result.ok(page);
    }

    @PreAuthorize("hasAuthority('system:log:delete')")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if(logService.deletelogById(id)){
            opalog("删除日志");
            return Result.ok().message("日志删除成功");
        }else {
            return Result.error().message("日志删除失败");
        }
    }

}
