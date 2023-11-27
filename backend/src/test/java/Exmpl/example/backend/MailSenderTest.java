package Exmpl.example.backend;
import Exmpl.Utils.sendEmailUtil;

import Exmpl.Utils.strUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MailSenderTest {

    @Autowired
    private sendEmailUtil mailUtil;

    //收件人
    private final String receiver = "937858146@qq.com";

    @Test
    public void testTemplateMail(){
        //发送邮件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String subject = "宠物之家--用户注册";
        String emailTemplate = "registerTemplate";
        String code = strUtils.getRandomString(5);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", receiver);
        dataMap.put("code", code);
        dataMap.put("createTime", sdf.format(new Date()));
        try {
            mailUtil.sendTemplateMail(receiver, subject, emailTemplate, dataMap);
            System.out.println("发送完成");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

}