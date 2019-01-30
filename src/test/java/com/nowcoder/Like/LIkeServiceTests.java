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

    // 初始化like数据，userId前10对前10个回答点赞。
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

    // 测试
    // 另外还有@Before，初始化数据
    // @After,清理数据

    @Test
    public void testLike() {
        likeService.like(999, EntityType.COMMENT, 999);
        Assert.assertEquals(1, likeService.getLikeStatus(999, EntityType.COMMENT, 999));

        likeService.disLike(1000, EntityType.COMMENT, 999);
        Assert.assertEquals(-1, likeService.getLikeStatus(1000, EntityType.COMMENT, 999));
    }
}
