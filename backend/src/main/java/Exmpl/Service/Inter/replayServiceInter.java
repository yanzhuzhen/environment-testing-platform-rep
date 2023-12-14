package Exmpl.Service.Inter;

import Exmpl.Entity.Replay;
import Exmpl.vo.query.replayQueryVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface replayServiceInter extends IService<Replay> {
    List<Replay> getReplayByCommentId(replayQueryVo replayQueryVo);

    boolean deleteReplayById(Long id);
}
