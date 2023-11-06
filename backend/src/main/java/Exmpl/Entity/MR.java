package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("MR")
public class MR {
   private Long mno;
   private Long rno;
}
