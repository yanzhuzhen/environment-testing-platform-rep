package com.Service;

import com.Entity.Menu;
import com.Mapper.menuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class menuService {
    @Autowired
    menuMapper menuMapper;
    public List<Menu> getAllMenus(){
        return menuMapper.selectList(null);
    }

}
