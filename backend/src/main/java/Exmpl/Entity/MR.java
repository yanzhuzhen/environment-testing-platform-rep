package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("MR")
public class MR {
    @TableId
    int id;
    int mno;
    int rno;
}
