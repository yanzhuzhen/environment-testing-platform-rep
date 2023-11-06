package Exmpl.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("menutable")
public class Menu {
    @TableId
    private Long mno;
    private String label;
    private String pname;
    private String name;
    private String url;
    private String path;
    private String icon;
    private String code;
    private Long pid;
    private int type;
    private int isdelete;

    @JsonInclude(JsonInclude.Include.NON_NULL) //属性值为null时，不进行序列化
    @TableField(exist = false) //表中无此字段，故为false
    private  List<Menu> childMenu = new ArrayList<Menu>(); //子菜单

    @TableField(exist = false)
    private String value; //用于判断是菜单还是按钮

    @TableField(exist = false)
    private boolean open; //菜单是否展开


}
