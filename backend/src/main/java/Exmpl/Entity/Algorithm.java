package Exmpl.Entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("algorithmtable")
public class Algorithm{
    @TableId
    private Long ano;
    private String algname;
    private String alg;
    private String createuser;
    private int ispermit;
    private int algtype;
}

