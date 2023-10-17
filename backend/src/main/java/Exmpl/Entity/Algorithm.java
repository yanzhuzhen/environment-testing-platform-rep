package Exmpl.Entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("algorithm")
public class Algorithm{
    @TableId
    private int ano;
    private String algName;
    private String algDate;
}
