package Exmpl.vo;

import Exmpl.Entity.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//角色权限菜单回显
@Data
public class roleMenuVo {
    //菜单数据
    private List<Menu> menuList = new ArrayList<Menu>();
    //该角色原有的菜单数据
    private Object[] checkList;
}
