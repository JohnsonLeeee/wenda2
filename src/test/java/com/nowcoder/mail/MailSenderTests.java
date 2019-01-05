package com.nowcoder.mail;

import com.nowcoder.WendaApplication;
import com.nowcoder.util.MailSender;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: wenda
 * @description: 测试com.nowcoder.utils.MailSender是否可用
 * @author: Li Shuai
 * @create: 2019-01-05 22:34
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class MailSenderTests {
    @Autowired
    MailSender mailSender;
    @Test
    public void contextLoads() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "杨超越");
        Assert.assertTrue(mailSender.sendWithHTMLTemplate(
                "13122386386@163.com", "登录IP异常", "mail/login_exception.html", map));
    }
}
