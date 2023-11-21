package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;

@Data
@TableName("ur")
public class UR {

    private Long uno;
    private Long rno;
}
