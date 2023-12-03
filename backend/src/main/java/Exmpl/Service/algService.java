package Exmpl.Service;

import Exmpl.Dao.algMapper;
import Exmpl.Entity.Alg;
import Exmpl.Service.Inter.algServiceInter;
import Exmpl.vo.query.algQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

@Service
public class algService extends ServiceImpl<algMapper, Alg> implements algServiceInter {
    @Resource
    algMapper algMapper;

    public IPage<Alg> findAlgList(IPage<Alg> page, algQueryVo algQueryVo) {
        QueryWrapper<Alg> queryWrapper = new QueryWrapper<Alg>();
        queryWrapper.like("createuser",algQueryVo.getCreateuser());
        queryWrapper.like(!ObjectUtils.isEmpty(algQueryVo.getAlgname()),"algname",algQueryVo.getAlgname());
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public Alg findAlgByAno(Long ano) {
            QueryWrapper<Alg> queryWrapper = new QueryWrapper<Alg>();
            queryWrapper.eq("ano",ano);
            return baseMapper.selectOne(queryWrapper);
    }


    @Override
    public Alg findAlgByAlgname(String algname) {
        QueryWrapper<Alg> queryWrapper = new QueryWrapper<Alg>();
        queryWrapper.eq("algname",algname);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean deleteAlgById(Long ano) {
        return baseMapper.deleteById(ano) > 0;
    }


}

