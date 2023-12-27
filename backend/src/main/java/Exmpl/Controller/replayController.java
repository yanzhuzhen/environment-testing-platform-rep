package Exmpl.Controller;

import Exmpl.Entity.Replay;
import Exmpl.Service.replayService;
import Exmpl.Utils.Result;
import Exmpl.vo.query.replayQueryVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static Exmpl.Utils.oparateLogUtils.opalog;

@Api(tags = "回复管理")
@RestController
@RequestMapping("/api/replay")
public class replayController {

    @Resource
    replayService replayService;

    @GetMapping("/list")
    public Result list(replayQueryVo replayQueryVo){
        List<Replay> replays = replayService.getReplayByCommentId(replayQueryVo);
        return Result.ok(replays);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Replay replay){
        if(replayService.save(replay)){
            opalog("回复成功");
            return Result.ok().message("回复成功");
        }else {
            return Result.error().message("回复失败");
        }
    }


    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if(replayService.deleteReplayById(id)){
            opalog("删除回复");
            return Result.ok().message("回复删除成功");
        }else {
            return Result.error().message("回复删除失败");
        }
    }



}
