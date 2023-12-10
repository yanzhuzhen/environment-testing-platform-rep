package Exmpl.Service;

import Exmpl.Dao.logMapper;
import Exmpl.Entity.Log;
import Exmpl.Service.Inter.logServiceInter;
import Exmpl.vo.query.logQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Service
public class logService extends ServiceImpl<logMapper, Log> implements logServiceInter {


    @Override
    public IPage<Log> findlogListByPage(IPage<Log> page, logQueryVo logQueryVo) {
        QueryWrapper<Log> queryWrapper = new QueryWrapper<Log>();
        queryWrapper.like(!ObjectUtils.isEmpty(logQueryVo.getLogname()),"logname",logQueryVo.getLogname());
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public boolean deletelogById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }
}

