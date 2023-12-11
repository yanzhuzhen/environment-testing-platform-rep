package Exmpl.Dao;

import Exmpl.Entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Mapper
public interface articleMapper extends BaseMapper<Article> {

    @Update("update article set status = 'published' where id = #{id}")
    int publish(Long id);
    @Update("update article set status = 'enable' where id = #{id}")
    int enable(Long id);
    @Insert("insert into likes(id, uno) values(#{id}, #{uno})")
    int likes(Long id, Long uno);

    @Select("select * from article where title = #{title}")
    Article findArticleByTitle(String title);

    @Select("select count(1) from likes where id = #{id} and uno = #{uno}")
    int alreadyLike(Long id, Long uno);

    @Select("select * from article where id = #{id}")
    Article findArticleById(Long id);

    @Select("select * from article where author = #{author} order by score desc limit 10")
    List<Article> getUserRankByAuthor(String author);

    @Select("select * from article order by score desc limit 20")
    List<Article> getRank();

    @Select("select * from article order by rand() limit 4")
    List<Article> getList();
}
