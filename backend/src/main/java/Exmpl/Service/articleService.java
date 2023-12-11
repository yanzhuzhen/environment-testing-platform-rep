package Exmpl.Service;

import Exmpl.Dao.articleMapper;
import Exmpl.Entity.Article;
import Exmpl.Service.Inter.articleServiceInter;
import Exmpl.vo.query.articleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class articleService extends ServiceImpl<articleMapper, Article> implements articleServiceInter {

    @Override
    public IPage<Article> findArticleList(IPage<Article> page, articleQueryVo articleQueryVo) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>();
        queryWrapper.like(!ObjectUtils.isEmpty(articleQueryVo.getTitle()),"title",articleQueryVo.getTitle());
        queryWrapper.like("status","enable");
        queryWrapper.orderByAsc("score");
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public List<Article> findHomePageList() {
        return baseMapper.getList();
    }

    @Override
    public IPage<Article> findArticleListM(IPage<Article> page, articleQueryVo articleQueryVo) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>();
        queryWrapper.like(!ObjectUtils.isEmpty(articleQueryVo.getTitle()),"title",articleQueryVo.getTitle());
        queryWrapper.like("status","published").or().like("status","enable");
        queryWrapper.orderByAsc("displaytime");
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public IPage<Article> findArticleListU(IPage<Article> page, articleQueryVo articleQueryVo) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>();
        queryWrapper.like("author",articleQueryVo.getAuthor());
        queryWrapper.orderByAsc("displaytime");
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public boolean deleteArticleById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public int likes(Long id, Long uno) {
        return baseMapper.likes(id, uno);
    }

    @Override
    public int alreadyLike(Long id, Long uno) {
        return baseMapper.alreadyLike(id, uno);
    }

    @Override
    public Article getArticle(Long id) {
        return baseMapper.findArticleById(id);
    }

    @Override
    public List<Article> getUserRankByAuthor(String author) {
        return baseMapper.getUserRankByAuthor(author);
    }

    @Override
    public List<Article> rank() {
        return baseMapper.getRank();
    }
}
