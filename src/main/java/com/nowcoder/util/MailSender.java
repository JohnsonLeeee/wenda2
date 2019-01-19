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

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

/**
 * @program: wenda
 * @description: 邮件发送
 * @author: Li Shuai
 * @create: 2019-01-05 21:13
 **/
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemplate(String to, String subject, String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("Wenda管理员");
            InternetAddress from = new InternetAddress(nick + "<1757670451@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // velocity引擎渲染
            String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            // 标题
            mimeMessageHelper.setSubject(subject);
            // 文本
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
            // |和||的区别同理，都可以作为逻辑或运算符；
            // |还可以作为按位或的运算符，运算规则与按位与同理；
            // ||同样有类似短路的功能，即第一个条件若为true，则不计算后面的表达式。
        } catch (UnsupportedEncodingException | MessagingException e) {
            logger.error("发送邮件失败");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("1757670451@qq.com");
        // qq邮箱的授权码
        mailSender.setPassword("reryntiglcmfciad");
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setDefaultEncoding("utf8");
        mailSender.setProtocol("smtps");
        Properties javaMailPropertier = new Properties();
        javaMailPropertier.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailPropertier);
    }
}
