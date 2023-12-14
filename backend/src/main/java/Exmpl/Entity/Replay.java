package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("replay")
public class Replay {
    private int id;
    private Long commentid;
    private Long uno;
    private String content;
    private String createtime;
}
