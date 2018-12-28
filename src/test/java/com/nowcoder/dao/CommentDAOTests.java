package com.nowcoder.dao;

import com.nowcoder.WendaApplication;
import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
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
 * @description: 测试Comment中addComment的返回值为什么
 * @author: Li Shuai
 * @create: 2018-12-24 22:58
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class CommentDAOTests {
    @Autowired
    CommentDAO commentDAO;

    @Test
    public void contextLoads() {
        Comment comment = new Comment();
        comment.setContent("测试");
        comment.setUserId(5);
        comment.setEntityType(EntityType.QUESTION);
        comment.setCreatedDate(new Date());
        comment.setStatus(0);
        comment.setEntityId(45);

        int returnedValue = commentDAO.addComment(comment);
        System.out.println(returnedValue);
        Assert.assertEquals(1, returnedValue);
    }
}
