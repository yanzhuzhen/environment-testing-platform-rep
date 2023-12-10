package Exmpl.Service.Inter;

import Exmpl.Entity.Alg;
import Exmpl.vo.query.algQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface algServiceInter extends IService<Alg> {
    public IPage<Alg> findAlgList(IPage<Alg> page, algQueryVo algQueryVo);

    IPage<Alg> findAlgListM(IPage<Alg> page, algQueryVo algQueryVo);

    public Alg findAlgByAno(Long ano);

    Alg findAlgByAlgname(String algname);

    boolean deleteAlgById(Long ano);

}
