package Exmpl.Service;

import Exmpl.Entity.Menu;
import Exmpl.Entity.User;
import Exmpl.Service.Inter.menuServiceInter;
import Exmpl.Utils.menuTree;
import Exmpl.vo.query.menuQueryVo;
import Exmpl.vo.roleMenuVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Exmpl.Dao.menuMapper;
import Exmpl.Dao.userMapper;
import org.springframework.util.ObjectUtils;


import javax.annotation.Resource;
import java.util.*;

@Service
public class menuService extends ServiceImpl<menuMapper, Menu> implements menuServiceInter {
    @Resource
    menuMapper menuMapper;

    @Resource
    userMapper userMapper;

    public List<Menu> getAllMenus(){
        return menuMapper.selectList(null);
    }

    @Override
    public List<Menu> findMenuListByUserId(Long uno) {
        return baseMapper.findMenuListByUserId((long) Math.toIntExact(uno));
    }

    @Override
    public List<Menu> findMenuList(menuQueryVo menuQueryVo) {
        //创建条件构造器对象
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        //查询菜单列表
        List<Menu> menuList = baseMapper.selectList(queryWrapper);
        //生成菜单树
        return menuTree.makeMenuTree(menuList, 0L);
    }

    @Override
    public List<Menu> findMenuParent() {
        //创建条件构造器对象
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        //只查询目录和菜单
        queryWrapper.in("type", Arrays.asList(0, 1));
        //设置排序字段
        queryWrapper.orderByAsc("order_num");
        //查询菜单列表
        List<Menu> menuList = baseMapper.selectList(queryWrapper);
        //构造一级菜单
        Menu menu = new Menu();
        menu.setMno(0L);
        menu.setMno_parent(-1L);
        menu.setTitle("一级菜单");
        menuList.add(menu);
        //生成菜单树
        return menuTree.makeMenuTree(menuList, -1L);
    }

    //判断是否有子菜单
    public boolean hasChildOfMenu(Long id) {
        //创建条件构造器对象
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        //查询父级ID
        queryWrapper.eq("mno_parent", id);
        //判断数量是否大于0 如大于则存在
        return baseMapper.selectCount(queryWrapper) > 0;
    }

    //查询权限菜单树
    @Override
    public roleMenuVo getAssignMenuTree(Long uno, Long rno) {
        //查询当前用户信息
        User user = userMapper.selectById(uno);
        List<Menu> list = null;
        //判断当前用户是否是管理员若是则查询所有权限，反之则只能查询自己的权限
        if(user != null && user.isAdmin()){
            //查询所有的权限菜单
            list = baseMapper.selectList(null);
        }else {
            //查询当前用户自己的权限菜单
            list = baseMapper.findMenuListByUserId(uno);
        }
        //生成树结构
        List<Menu> menuList = menuTree.makeMenuTree(list, 0L);
        //查询要分配角色的原有权限
        List<Menu> roleMenuList = baseMapper.findMenuListByRoleId(rno);
        //进行数据回显
        List<Long> listIds = new ArrayList<Long>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .forEach(i -> {
                    Optional.ofNullable(roleMenuList).orElse(new ArrayList<>())
                            .stream()
                            .filter(Objects::nonNull)
                            .forEach(j ->{
                                //判断两者权限id是否相同，相同则说明拥有该权限，并保存到listIds中
                                if(i.getMno().equals(j.getMno())){
                                    listIds.add(i.getMno());
                                }
                            });
                });
        roleMenuVo vo = new roleMenuVo();
        vo.setMenuList(menuList);
        vo.setCheckList(listIds.toArray());
        return vo;
    }
}
