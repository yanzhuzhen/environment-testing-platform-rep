package Exmpl.Service.Inter;

import Exmpl.Entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface menuServiceInter extends IService<Menu> {
    List<Menu> findMenuListByUserId(@Param("uno") int uno);
}
