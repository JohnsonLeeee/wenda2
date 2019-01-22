package com.nowcoder.Like;

import com.nowcoder.WendaApplication;
import com.nowcoder.model.EntityType;
import com.nowcoder.service.LikeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @program: wenda
 * @description: 测试likeService是否可用
 * @author: Li Shuai
 * @create: 2019-01-01 20:47
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class LIkeServiceTests {
    @Autowired
    LikeService likeService;
    @Test
    public void contextLoads() {
        System.out.println(likeService.like(2, EntityType.COMMENT, 2));

        for (int entityId = 0; entityId < 20; entityId++) {
            for (int userId = 0; userId < 10; userId++) {
                likeService.like(userId, EntityType.COMMENT, entityId);
            }
            Assert.assertTrue(likeService.getLikeCountByEntityId(EntityType.COMMENT, entityId) > 0);
        }
        System.out.println(likeService.getLikeCountByEntityId(EntityType.COMMENT, 2));
    }
}
