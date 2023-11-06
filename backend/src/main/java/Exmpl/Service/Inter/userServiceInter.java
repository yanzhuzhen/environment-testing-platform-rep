package Exmpl.Service.Inter;


import Exmpl.Entity.User;
import Exmpl.vo.query.userQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface userServiceInter  extends IService<User> {
    User findUserByUsername(String username);

    IPage<User> findUserListByPage(IPage<User> page, userQueryVo userQueryVo);

    boolean deleteById(Long uno);

    boolean saveUserRole(Long uno, List<Long> rnoList);

    boolean deleteRole(Long uno);
}
