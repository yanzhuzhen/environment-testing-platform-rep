package Exmpl.Service.Inter;

import Exmpl.Entity.Alg;
import Exmpl.Entity.Follows;
import Exmpl.vo.query.algQueryVo;
import Exmpl.vo.query.followsQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface followsServiceInter extends IService<Follows> {
    public IPage<Follows> findFollowsList(IPage<Follows> page, followsQueryVo followsQueryVo);

    boolean deleteAlgById(Long id);

    Long findFollowsByUnoAndFno(Long uno, Long fno);
}
