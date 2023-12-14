package Exmpl.Dao;

import Exmpl.Entity.Comment;
import Exmpl.Entity.Replay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface replayMapper extends BaseMapper<Replay> {
    @Select("select * from replay where commentid = #{replay.commentid}")
    List<Replay> getReplayByCommentId(Replay replay);
}
