package Exmpl.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
public class sendEmailUtil {
    //发件人
    private final String sender="937858146@qq.com";
    @Resource
    JavaMailSender javaMailSender;
    @Resource
    TemplateEngine templateEngine;

    /**
     * 发送邮件
     * @param receiver 收件人
     * @param subject  邮件标题
     * @param emailTemplate HTML模板
     * @param dataMap  模板中的数据
     * @throws Exception
     */
    @Async//异步发送注解
    public void sendTemplateMail(String receiver, String subject, String emailTemplate, Map<String, Object> dataMap) throws Exception {
        //给模板引擎提供变量
        Context context = new Context();
        //设置传入模板页面的值   从dataMap中获取
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());//例如:context.setVariable("code",123)
        }
        //emailTemplate是你要发送的模板，我这里用的是Thymeleaf  emailTemplate="registerTemplate"
        String templateContent = templateEngine.process(emailTemplate, context);

        //准备发送邮件
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //设置发件人
        helper.setFrom(sender);
        //设置收件人
        helper.setTo(receiver);
        //设置标题
        helper.setSubject(subject);
        //设置内容
        helper.setText(templateContent, true);
        javaMailSender.send(message);
    }
}