package Exmpl.Controller;

import Exmpl.Dao.articleMapper;
import Exmpl.Dao.incMapper;
import Exmpl.Entity.Alg;
import Exmpl.Entity.Article;
import Exmpl.Service.articleService;
import Exmpl.Utils.Result;
import Exmpl.vo.query.algQueryVo;
import Exmpl.vo.query.articleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/api/article")
public class articleController {

    @Resource
    articleService articleService;

    @Resource
    articleMapper articleMapper;

    @Resource
    incMapper incMapper;

    @GetMapping("/listM")
    public Result listM(articleQueryVo articleQueryVo){
        IPage<Article> page = new Page<Article>(articleQueryVo.getPageNow(), articleQueryVo.getPageSize());
        articleService.findArticleListM(page, articleQueryVo);
        return Result.ok(page);

    }

    @GetMapping("/list")
    public Result list(articleQueryVo articleQueryVo){
        IPage<Article> page = new Page<Article>(articleQueryVo.getPageNow(), articleQueryVo.getPageSize());
        articleService.findArticleList(page, articleQueryVo);
        return Result.ok(page);

    }

    @GetMapping("/listU")
    public Result listU(articleQueryVo articleQueryVo){
        IPage<Article> page = new Page<Article>(articleQueryVo.getPageNow(), articleQueryVo.getPageSize());
        articleService.findArticleListU(page, articleQueryVo);
        return Result.ok(page);

    }

    @PostMapping("/add")
    public Result add(@RequestBody Article article){
        Article a = articleMapper.findArticleByTitle(article.getTitle());
        if(a != null){
            return Result.error().message("该标题已存在，请勿使用已有的标题");
        }
        article.setContent(HtmlUtils.htmlEscape(article.getContent()));
        Long id = incMapper.findAllArticle() + 1;
        article.setId(id);
        if(articleService.save(article)){
            return Result.ok().message("文章草稿保存成功");
        }else {
            return Result.error().message("文章草稿保存失败");
        }
    }

    @PutMapping("/publish")
    public Result publish(@RequestBody Article article){
        Article a = articleMapper.findArticleByTitle(article.getTitle());
        if(articleMapper.publish(a.getId())>0){
            return Result.ok().message("文章发布成功");
        }else {
            return Result.error().message("文章发布失败");
        }
    }

    @PutMapping("/enable/{id}")
    public Result enable(@PathVariable Long id){
        if(articleMapper.enable(id)>0){
            return Result.ok().message("文章审核成功");
        }else {
            return Result.error().message("文章审核失败");
        }
    }


    @PutMapping("/update")
    public Result update(@RequestBody Article article){
        if(articleService.updateById(article)){
            return Result.ok().message("文章修改成功");
        }else {
            return Result.error().message("文章修改失败");
        }
    }


    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if(articleService.deleteArticleById(id)){
            return Result.ok().message("文章删除成功");
        }else {
            return Result.error().message("文章删除失败");
        }
    }

    @PostMapping("/likes/{id}/{uno}")
    public Result likes(@PathVariable Long id, @PathVariable Long uno){
        if(articleService.likes(id, uno)>0){
            return Result.ok().message("添加喜欢成功");
        }else {
            return Result.error().message("添加喜欢失败");
        }
    }

    @GetMapping("/alreadyLike/{id}/{uno}")
    public Result alreadyLike(@PathVariable Long id, @PathVariable Long uno){
        if(articleService.alreadyLike(id, uno)>0){
            return Result.ok(true);
        }else {
            return Result.error(false);
        }
    }

    @GetMapping("/getEditArticle/{id}")
    public Result getEditArticle(@PathVariable Long id){
        Article article = articleService.getArticle(id);
        if(article != null){
            return Result.ok(article);
        }else {
            return Result.error();
        }
    }

    @GetMapping("/getArticle/{id}")
    public Result getArticle(@PathVariable Long id){
        Article article = articleService.getArticle(id);
        article.setContent(HtmlUtils.htmlUnescape(article.getContent()));
        if(article != null){
            return Result.ok(article);
        }else {
            return Result.error();
        }
    }

}
