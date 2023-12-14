package Exmpl.Service;

import Exmpl.Dao.replayMapper;
import Exmpl.Entity.Replay;
import Exmpl.Service.Inter.replayServiceInter;
import Exmpl.vo.query.replayQueryVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class replayService extends ServiceImpl<replayMapper, Replay> implements replayServiceInter {

    @Override
    public List<Replay> getReplayByCommentId(replayQueryVo replayQueryVo) {
        return baseMapper.getReplayByCommentId(replayQueryVo);
    }

    @Override
    public boolean deleteReplayById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }
}
