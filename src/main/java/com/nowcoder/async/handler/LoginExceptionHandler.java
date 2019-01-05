package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: wenda
 * @description: 登录IP异常，给用户邮箱发一封信
 * @author: Li Shuai
 * @create: 2019-01-05 18:26
 **/

//@component 的作用：
//相当于配置文件中applicationContext.xml中的一句：<bean id="XXX" class="XXXXX"></bean>
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {

        // todo：判断发现用户登录异常
        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExts("username"));
        mailSender.sendWithHTMLTemplate(
                model.getExts("email"), "登录IP异常", "mail/login_exception.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
