package Exmpl.Utils;

import Exmpl.Entity.Menu;
import Exmpl.vo.routerVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//生成菜单树
public class menuTree {
    public static List<routerVo> makeRouter(List<Menu> menuList, Long pid){
        //创建集合保存路由信息,并判空；不为空则创建菜单列表，为空创建集合对象防止空指针
        List<routerVo> routerVoList = new ArrayList<routerVo>();
        Optional.ofNullable(menuList).orElse(new ArrayList<Menu>())
                .stream()
                .filter(item -> item != null && Objects.equals(item.getPid(), pid)) //筛选不为空的菜单及菜单父ID相同的数据
                .forEach(item ->{
                    routerVo routerVo = new routerVo(); //创建路由信息对象
                    routerVo.setName(item.getName());
                    routerVo.setPath(item.getPath());
                    //判断是否是一级菜单
                    if(item.getPid() == 0L){
                        routerVo.setComponent("Layout"); //一级菜单组件
                        routerVo.setAlwaysShow(true); //显示路由
                    }else {
                        routerVo.setComponent(item.getUrl());
                        routerVo.setAlwaysShow(false);
                    }
                    routerVo.setMeta(routerVo.new Meta(item.getLabel(), item.getIcon(), item.getCode().split(","))); //设置meta
                    //递归生成路由菜单树
                    List<routerVo> child = makeRouter(menuList, item.getMno()); //生成子菜单
                    routerVo.setChildren(child); //把子路由放到路由菜单中

                    routerVoList.add(routerVo); //将路由信息添加到集合中
                });
        return routerVoList;
    }

    public static List<Menu> makeMenuTree(List<Menu> menuList, Long pid){
        //创建集合保存菜单信息,并判空；不为空则创建菜单列表，为空创建集合对象防止空指针
        List<Menu> menuTreeList = new ArrayList<Menu>();
        Optional.ofNullable(menuList).orElse(new ArrayList<Menu>())
                .stream()
                .filter(item -> item!=null&&item.getPid().equals(pid))
                .forEach(item ->{
                    Menu menu = new Menu(); //创建菜单
                    BeanUtils.copyProperties(item,menu); //将原有对象的属性复制给新的菜单对象
                    List<Menu> child = makeMenuTree(menuList, item.getMno()); //获取每一个item对象的子菜单，递归生成菜单树
                    menu.setChildren(child); //设置子菜单
                    menuTreeList.add(menu); //将菜单对象加到集合
                });
        return menuTreeList;
    }


}

