package Exmpl.Service.Inter;

import Exmpl.Entity.Article;
import Exmpl.vo.query.articleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface articleServiceInter extends IService<Article> {
    public IPage<Article> findArticleList(IPage<Article> page, articleQueryVo articleQueryVo);

    List<Article> findHomePageList();

    IPage<Article> findArticleListM(IPage<Article> page, articleQueryVo articleQueryVo);

    IPage<Article> findArticleListU(IPage<Article> page, articleQueryVo articleQueryVo);

    boolean deleteArticleById(Long id);
    int likes(Long id, Long uno);

    int alreadyLike(Long id, Long uno);

    Article getArticle(Long id);


    List<Article> getUserRankByAuthor(String author);

    List<Article> rank();

    int isscored(Long articleid, Long uno);


    boolean score(Long articleid, Long uno, int score);
}
