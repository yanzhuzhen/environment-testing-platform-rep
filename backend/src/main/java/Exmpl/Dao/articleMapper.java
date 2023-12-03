package Exmpl.Dao;

import Exmpl.Entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
}
