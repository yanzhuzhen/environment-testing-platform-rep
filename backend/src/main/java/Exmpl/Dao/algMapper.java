package Exmpl.Dao;

import Exmpl.Entity.Alg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface algMapper extends BaseMapper<Alg> {

    @Update("update algorithmtable set ispermit = 1 where ano = #{ano}")
    int ispermit(Long ano);

}
