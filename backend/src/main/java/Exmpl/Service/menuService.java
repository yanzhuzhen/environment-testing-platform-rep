package Exmpl.Service;

import Exmpl.Entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class menuService {
    @Autowired
    Exmpl.Mapper.menuMapper menuMapper;
    public List<Menu> getAllMenus(){
        return menuMapper.selectList(null);
    }

}