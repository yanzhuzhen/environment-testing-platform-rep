package Exmpl.Service;

import Exmpl.Dao.algMapper;
import Exmpl.Dao.followsMapper;
import Exmpl.Entity.Alg;
import Exmpl.Entity.Follows;
import Exmpl.Service.Inter.algServiceInter;
import Exmpl.Service.Inter.followsServiceInter;
import Exmpl.vo.query.followsQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class followsService extends ServiceImpl<followsMapper, Follows> implements followsServiceInter {
    @Override
    public IPage<Follows> findFollowsList(IPage<Follows> page, followsQueryVo followsQueryVo) {
        QueryWrapper<Follows> queryWrapper = new QueryWrapper<Follows>();
        queryWrapper.like(!ObjectUtils.isEmpty(followsQueryVo.getUsername()),"username",followsQueryVo.getUsername());
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public boolean deleteAlgById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public Long findFollowsByUnoAndFno(Long uno, Long fno) {
        return null;
    }
}
