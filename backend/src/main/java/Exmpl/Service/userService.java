package Exmpl.Service;

import Exmpl.Dto.userDTO;
import Exmpl.Entity.User;
import Exmpl.Dao.userMapper;
import Exmpl.Dao.incMapper;
import Exmpl.Service.Inter.userServiceInter;
import Exmpl.Utils.Result;
import Exmpl.Utils.sendEmailUtil;
import Exmpl.Utils.strUtils;
import Exmpl.Utils.systemConstants;
import Exmpl.vo.query.userQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class userService extends ServiceImpl<userMapper, User> implements userServiceInter {

    @Resource
    fileService fileService;

    @Resource
    redisService redisService;

    @Resource
    sendEmailUtil sendEmailUtil;

    @Resource
    BCryptPasswordEncoder passwordEncoder;

    @Resource
    incMapper incMapper;

    @Resource
    userMapper userMapper;

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
    public boolean deleteUserById(Long uno) {
        User user = baseMapper.selectById(uno);
        baseMapper.deleteUserRole(uno);
        boolean flag = false;
        if(baseMapper.deleteById(uno)>0){
            flag = true;
            if(user != null && !ObjectUtils.isEmpty(user.getAvatar()) && !user.getAvatar().equals(systemConstants.default_avatar)){
                fileService.deleteFile(user.getAvatar());
            }
        }
        return flag;
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

    @Override
    public Result registerEmail(userDTO userDTO){
        try {
            //判断密码是否输入
            if(StringUtils.isBlank(userDTO.getPassword())||StringUtils.isBlank(userDTO.getConfirmPassword()))
                return Result.error().message("密码不能为空");
            //如果两次面不相等
            if(!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
                return Result.error().message("密码不一致");

            //保存对象
            User user=new User();
            int u = userMapper.findUserByEmail(userDTO.getEmail());
            if (u != 0) {
                return Result.error().message("该邮箱已注册");
            }
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            //给用户密码进行盐值加密
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            //保存用户
            Long id = incMapper.findAllUser() + 1;
            user.setUno(id);
            user.setEnabled(false);
            int i = userMapper.saveUser(user);
            System.out.println(i);
            //给刚注册好的用户发送邮件
            testTemplateMail(user.getEmail());
            return Result.ok().message("邮件已发送至邮箱，请在10分钟内完成!");
        } catch (Exception e){
            e.printStackTrace();
            return Result.error().message("获取验证码失败");
        }
    }

    /**
     * 用户激活
     *
     * @param email 邮箱
     * @param code  激活码
     * @return
     */
    @Override
    public Result activation(String email, String code) {
        //判断输入是否为空
        if(StringUtils.isBlank(email)||StringUtils.isBlank(code)){
            return Result.error().message("请输入邮箱或验证码");
        }
        //校验激活码是否正常

        // redis服务器上获取的激活码
        String s = redisService.getCache("Action:" + email);
        System.out.println("redis服务器上获取的激活码为:"+s);
        //从redis服务器中获取code值，如果code值为空，说明激活码错误或者已过期!
        if(s==null)
            return Result.error().message("邮箱或验证码错误或者已过期");
        //激活码是否正确
        if(!code.equals(s))//不正确
        {
            return Result.error().message("验证码错误或者已过期");
        }

        //激活后从redis服务器上移除
        redisService.delCache("Action:" + email);

        //用户激活
        User user = baseMapper.getUserByEmail(email);
        user.setEnabled(true);
        baseMapper.isEnable(user.getEmail());
        baseMapper.setUR(user.getUno());


        return Result.ok().message("注册成功，请使用注册的账号登录");
    }


    @Override
    public Result updatePasswordByEmail(userDTO userDTO) {
        try {
            User user =  userMapper.getUserByEmail(userDTO.getEmail());
            //判断是否有该用户
            if(user == null)
                return Result.error().message("未找到使用此邮箱的用户！");
            //判断密码是否输入
            if(StringUtils.isBlank(userDTO.getPassword())||StringUtils.isBlank(userDTO.getConfirmPassword()))
                return Result.error().message("密码不能为空！");
            //如果两次面不相等
            if(!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
                return Result.error().message("密码不一致！");
            //不能与原密码相同
            boolean matches = passwordEncoder.matches(userDTO.getPassword(), user.getPassword());
            if(matches)
                return Result.error().message("新密码不能与原密码相同！");


            //给用户密码进行盐值加密
            baseMapper.updatePassword(passwordEncoder.encode(userDTO.getPassword()), userDTO.getEmail());
            baseMapper.isNonEnable(userDTO.getEmail()); //暂时失效，等待激活
            user.setEnabled(false);
            testTemplateMail(user.getEmail());
            return Result.ok().message("邮件已发送至邮箱，请在10分钟内完成!");
        } catch (Exception e){
            e.printStackTrace();
            return Result.error().message("获取验证码失败");
        }
    }

    /**
     * 修改密码激活
     *
     * @param email 邮箱
     * @param code  激活码
     * @return
     */
    @Override
    public Result activationPassword(String email, String code) {
        //判断输入是否为空
        if(StringUtils.isBlank(email)||StringUtils.isBlank(code)){
            return Result.error().message("请输入邮箱或验证码");
        }
        //校验激活码是否正常

        // redis服务器上获取的激活码
        String s = redisService.getCache("Action:" + email);
        System.out.println("redis服务器上获取的激活码为:"+s);
        //从redis服务器中获取code值，如果code值为空，说明激活码错误或者已过期!
        if(s==null)
            return Result.error().message("邮箱或验证码错误或者已过期");
        //激活码是否正确
        if(!code.equals(s))//不正确
        {
            return Result.error().message("验证码错误或者已过期");
        }

        //激活后从redis服务器上移除
        redisService.delCache("Action:" + email);

        //用户激活
        User user = baseMapper.getUserByEmail(email);
        user.setEnabled(true);
        baseMapper.isEnable(user.getEmail());

        return Result.ok().message("密码修改成功，请使用新的密码登录");
    }




    /**
     * 发送邮件
     * @param receiver 收件人
     */
    @Async
    public void testTemplateMail(String receiver){
        //发送邮件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String subject = "西南地区国土环境监测学习平台--用户注册或修改密码";
        String emailTemplate = "registerTemplate";
        //获取激活码
        String code = strUtils.getRandomString(5);
        //将激活码存入redis服务器
        //设置有效期10分钟  key值为:Action+邮箱 value值为:验证码
        redisService.setCache("Action:"+receiver,code, (long) (10*60));
        System.out.println("key:"+"Action:"+receiver+"   value:"+code);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", receiver);
        dataMap.put("code", code);
        dataMap.put("createTime", sdf.format(new Date()));
        try {
            sendEmailUtil.sendTemplateMail(receiver, subject, emailTemplate, dataMap);
            System.out.println("发送完成");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }






    @Override
    public String findAvatarByUno(Long id) {
        return userMapper.findAvatarByUno(id);
    }


}


