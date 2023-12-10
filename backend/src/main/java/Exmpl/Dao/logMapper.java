package Exmpl.Dao;

import Exmpl.Entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface logMapper extends BaseMapper<Log> {
    @Insert("insert into log(loglevel,logcontent,logtime,logclass,logthread,trackid) values(#{loglevel},#{logcontent},#{logtime},#{logclass},#{logthread},#{trackid})")
    int saveLog(String loglevel, String logcontent, String logtime, String logclass, String logthread, String trackid) ;

}
