package Exmpl.Service.Inter;

import Exmpl.Entity.Log;
import Exmpl.vo.query.logQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface logServiceInter extends IService<Log> {

    IPage<Log> findlogListByPage(IPage<Log> page, logQueryVo logQueryVo);

    boolean deletelogById(Long id);

}
