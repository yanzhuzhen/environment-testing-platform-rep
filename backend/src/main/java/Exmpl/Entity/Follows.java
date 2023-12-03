package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("follows")
public class Follows {
    private Long id;
    private Long uno;
    private String username;
    private Long fno;
    private String fname;
    private Date date;
}
