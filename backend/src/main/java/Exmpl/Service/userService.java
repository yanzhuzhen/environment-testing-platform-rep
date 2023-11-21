package Exmpl.Service;

import Exmpl.Entity.User;
import Exmpl.Dao.userMapper;

import Exmpl.Service.Inter.userServiceInter;
import Exmpl.Utils.systemConstants;
import Exmpl.vo.query.userQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class userService extends ServiceImpl<userMapper, User> implements userServiceInter {

    @Resource
    fileService fileService;

    @Override
    public User findUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",username);

        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<User> findUserListByPage(IPage<User> page, userQueryVo userQueryVo) {
        //创建条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getUsername()),"username",userQueryVo.getUsername());
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    public boolean deleteById(Long uno) {
        User user = baseMapper.selectById(uno);
        baseMapper.deleteUserRole(uno);
        if(baseMapper.deleteById(uno)>0){
            if(user != null && !ObjectUtils.isEmpty(user.getAvatar()) && !user.getAvatar().equals(systemConstants.default_avatar)){
                fileService.deleteFile(user.getAvatar());
            }
        }
        return baseMapper.deleteUserRole(uno)>0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean saveUserRole(Long uno, List<Long> rnoList) {
        baseMapper.deleteUserRole(uno);
        return baseMapper.saveUserRole(uno,rnoList) > 0;
    }

    @Override
    public boolean deleteRole(Long uno) {
        return baseMapper.deleteUserRole(uno)>0;
    }


}


