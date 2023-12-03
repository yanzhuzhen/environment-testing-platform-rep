package Exmpl.Entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("algorithmtable")
public class Alg {
    @TableId
    private Long ano;
    private String algname;
    private String remark;
    private String context;
    private String createuser;
    private String createuno;
    private int ispermit;
    private int algtype;
    private String lang;
}

