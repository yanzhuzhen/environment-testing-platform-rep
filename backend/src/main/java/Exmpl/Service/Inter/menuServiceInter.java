package Exmpl.Service.Inter;

import Exmpl.Entity.Menu;
import Exmpl.vo.query.menuQueryVo;
import Exmpl.vo.roleMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface menuServiceInter extends IService<Menu> {
    //根据用户ID查询菜单
    List<Menu> findMenuListByUserId(Long uno);
    //查询菜单列表
    List<Menu> findMenuList(menuQueryVo menuQueryVo);
    //查询父菜单
    List<Menu> findMenuParent();

    //判断是否有子菜单
    boolean hasChildOfMenu(Long id);

    //查询分配权限树列表
    roleMenuVo getAssignMenuTree(Long uno, Long rno );

    boolean deleteMenuById(Long mno);

}
