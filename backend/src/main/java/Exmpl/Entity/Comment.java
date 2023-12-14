package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mycomment")
public class Comment {
    private int id;
    private Long articleid;
    private String username;
    private String content;
    private String createtime;
}
