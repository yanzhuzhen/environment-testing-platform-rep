package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;

@Data
@TableName("mr")
public class   MR {
   private Long mno;
   private Long rno;
}
