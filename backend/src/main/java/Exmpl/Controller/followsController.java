package Exmpl.Controller;

import Exmpl.Dao.incMapper;
import Exmpl.Dao.userMapper;
import Exmpl.Dto.followsDTO;
import Exmpl.Entity.Follows;
import Exmpl.Service.followsService;
import Exmpl.Utils.Result;
import Exmpl.vo.query.followsQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Api(tags = "粉丝管理")
@RestController
@RequestMapping("/api/follows")
public class followsController {

    @Resource
    followsService followsService;
    @Resource
    incMapper incMapper;
    @Resource
    userMapper userMapper;



    @GetMapping("/list")
    public Result list(followsQueryVo followsQueryVo){
        IPage<Follows> page = new Page<Follows>(followsQueryVo.getPageNow(), followsQueryVo.getPageSize());
        followsService.findFollowsList(page,followsQueryVo);
        return Result.ok(page);

    }

    @PostMapping("/add")
    public Result add(@RequestBody followsDTO followsDTO){
        Long id = incMapper.findAllFollows() + 1;
        followsDTO.setId(id);
        followsDTO.setFname(userMapper.findUserById(followsDTO.getFno()));
        followsDTO.setUsername(userMapper.findUserById(followsDTO.getUno()));
        followsDTO.setDate(new Date());
        if(followsService.save(followsDTO)){
            return Result.ok().message("关注成功");
        }else {
            return Result.error().message("关注失败");
        }
    }

    @DeleteMapping("/delete/")
    public Result delete(@RequestBody followsDTO followsDTO){
        Long id = followsService.findFollowsByUnoAndFno(followsDTO.getUno(), followsDTO.getFno());
        if(followsService.deleteAlgById(id)){
            return Result.ok().message("取消关注成功");
        }else {
            return Result.error().message("取消关注失败");
        }
    }
}
