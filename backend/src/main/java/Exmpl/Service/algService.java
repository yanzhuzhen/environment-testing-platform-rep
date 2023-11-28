package Exmpl.Service;

import Exmpl.Dao.algMapper;
import Exmpl.Dao.userMapper;
import Exmpl.Entity.Alg;
import Exmpl.Entity.Menu;
import Exmpl.Entity.User;
import Exmpl.Service.Inter.algServiceInter;
import Exmpl.Service.Inter.userServiceInter;
import Exmpl.vo.query.algQueryVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class algService extends ServiceImpl<algMapper, Alg> implements algServiceInter {
    @Resource
    algMapper algMapper;

    public IPage<Alg> findAlgList(IPage<Alg> page, algQueryVo algQueryVo) {
        QueryWrapper<Alg> queryWrapper = new QueryWrapper<Alg>();
        queryWrapper.like(!ObjectUtils.isEmpty(algQueryVo.getAlgname()),"algname",algQueryVo.getAlgname());
        return baseMapper.selectPage(page,queryWrapper);
    }

}

