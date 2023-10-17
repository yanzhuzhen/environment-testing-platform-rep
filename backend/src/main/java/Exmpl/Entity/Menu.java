package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("menu")
public class Menu {
    @TableId
    private int mno;
    private char mname;
    private String url;
    private List<Role> role;
}
