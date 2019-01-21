package com.nowcoder.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: pyh
 * @Date: 2019/1/20 21:12
 * @Version 1.0
 * @Function:
 *      实现发邮件功能
 */
@Service
public class MailSender implements InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;//引入javax库的发邮件功能

    @Autowired
    private VelocityEngine velocityEngine;

    //发邮件  to:发送对象，subject：标题   template：发送该邮件所用的模板 model：模板中变量字段的替换
    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model){
        try {
            String nick = MimeUtility.encodeText("类知乎网——邮件发送平台");//指定名字
            InternetAddress from = new InternetAddress(nick + "<**********************@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();//创建邮件的正文
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);//用于设置正文
            String result= VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);//设置文本，使用velocity自带渲染引擎
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e){
            logger.error("邮件发送失败 ： " + e.getMessage());
            return false;
        }
    }

    //邮件初始化
    @Override
    public void afterPropertiesSet() throws Exception {
        //在这里设置自己的邮箱
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("**********************@qq.com");//用户名**********************
        mailSender.setPassword("**********************");//授权码，不是账号密码
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
