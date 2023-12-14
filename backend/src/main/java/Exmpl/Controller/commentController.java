package Exmpl.Controller;

import Exmpl.Entity.Alg;
import Exmpl.Entity.Comment;
import Exmpl.Entity.User;
import Exmpl.Service.commentService;
import Exmpl.Service.replayService;
import Exmpl.Utils.Result;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static Exmpl.Utils.oparateLogUtils.opalog;

@RestController
@RequestMapping("/api/comment")
public class commentController {
    @Resource
    commentService commentService;

    @GetMapping("/list")
    public Result list(Comment comment){
        List<Comment> comments = commentService.getCommentByArticleId(comment);
        return Result.ok(comments);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Comment comment){
        comment.setCreatetime(String.valueOf(new Date()));
        if(commentService.save(comment)){
            opalog("评论成功");
            return Result.ok().message("评论成功");
        }else {
            return Result.error().message("评论失败");
        }
    }


    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if(commentService.deleteCommentById(id)){
            opalog("删除评论");
            return Result.ok().message("评论删除成功");
        }else {
            return Result.error().message("评论删除失败");
        }
    }

}
