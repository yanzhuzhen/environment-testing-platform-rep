package Exmpl.Service.Inter;

import Exmpl.Entity.Alg;
import Exmpl.Entity.Menu;
import Exmpl.Entity.User;
import Exmpl.vo.query.algQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface algServiceInter extends IService<Alg> {
    public IPage<Alg> findAlgList(IPage<Alg> page, algQueryVo algQueryVo);
}
