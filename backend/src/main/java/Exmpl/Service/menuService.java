package Exmpl.Service;

import Exmpl.Entity.Menu;
import Exmpl.Service.Inter.menuServiceInter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Exmpl.Dao.menuMapper;
import java.util.List;

@Service
public class menuService extends ServiceImpl<menuMapper, Menu> implements menuServiceInter {
    @Autowired
    menuMapper menuMapper;
    public List<Menu> getAllMenus(){
        return menuMapper.selectList(null);
    }

    @Override
    public List<Menu> findMenuListByUserId(int uno) {
        return baseMapper.findMenuListByUserId(uno);
    }
}
