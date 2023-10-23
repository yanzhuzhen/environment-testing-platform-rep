package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("menu")
public class Menu {
    @TableId
    private int mno;
    private char mname;
    private String url;
    private String icon;
    private int mno_parent;
    private List<Role> role;

    @JsonInclude(JsonInclude.Include.NON_NULL) //属性值为null时，不进行序列化
    @TableField(exist = false) //表中无此字段，故为false
    private  List<Menu> childMenu = new ArrayList<Menu>();

    @TableField(exist = false)
    private String value; //用于判断是菜单还是按钮

    @TableField(exist = false)
    private boolean isOpen; //菜单是否展开


}
