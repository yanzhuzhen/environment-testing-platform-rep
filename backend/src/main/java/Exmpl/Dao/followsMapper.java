package Exmpl.Dao;

import Exmpl.Entity.Follows;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface followsMapper extends BaseMapper<Follows> {
    @Select("select id from follows where uno=#{uno} and fno=#{fno}")
    Long findFollowsByUnoAndFno(Long uno, Long fno);
}
