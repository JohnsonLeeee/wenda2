package com.nowcoder.dao;

import com.nowcoder.WendaApplication;
import com.nowcoder.model.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * @program: wenda
 * @description: 测试messageDAO的语句是否正确
 * @author: Li Shuai
 * @create: 2018-12-25 11:08
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class MessageDAOTests {

    @Autowired
    MessageDAO messageDAO;
    @Test
    public void contextLoads() {
        Message message = new Message();
        message.setFromId(3);
        message.setToId(4);
        message.setContent("测试messageDAO是否可用");
        message.setConversationId(3, 4);
        message.setCreatedDate(new Date());
        message.setHasRead(false);
        int returnedValue = messageDAO.addMessage(message);

        Assert.assertEquals(1, returnedValue);
    }
}
