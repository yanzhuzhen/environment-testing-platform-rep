package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("likes")
public class likes {
    private Long id;//文章id
    private Long uno;//用户id
    private Date date;
}
