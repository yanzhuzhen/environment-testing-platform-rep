package Exmpl.Service;

import Exmpl.Dao.commentMapper;
import Exmpl.Entity.Comment;
import Exmpl.Service.Inter.commentServiceInter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class commentService extends ServiceImpl<commentMapper, Comment> implements commentServiceInter {

    @Override
    public List<Comment> getCommentByArticleId(Comment comment) {
        return baseMapper.getCommentByArticleId(comment.getArticleid());
    }

    @Override
    public boolean deleteCommentById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }
}
