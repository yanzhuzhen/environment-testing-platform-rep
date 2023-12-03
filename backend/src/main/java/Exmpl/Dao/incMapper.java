package Exmpl.Dao;

import Exmpl.Entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface incMapper {
    //查询用户数量
    @Select("select count(1) from usertable")
    Long findAllUser();
    @Select("select count(1) from roletable")
    Long findAllRole();
    @Select("select count(1) from menutable")
    Long findAllMenu();
    @Select("select count(1) from algorithmtable")
    Long findAllAlg();
    @Select("select count(1) from follows")
    Long findAllFollows();
    @Select("select count(1) from article")
    Long findAllArticle();
}
