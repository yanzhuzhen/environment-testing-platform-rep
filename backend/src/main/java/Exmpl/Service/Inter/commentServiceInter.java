package Exmpl.Service.Inter;

import Exmpl.Entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface commentServiceInter extends IService<Comment> {

    List<Comment> getCommentByArticleId(Comment comment);


    boolean deleteCommentById(Long id);
}
