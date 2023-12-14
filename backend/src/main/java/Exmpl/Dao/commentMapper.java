package Exmpl.Dao;

import Exmpl.Entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface commentMapper extends BaseMapper<Comment> {
    @Select("select * from mycomment where articleid = #{id}")
    List<Comment> getCommentByArticleId(Long id);
}
