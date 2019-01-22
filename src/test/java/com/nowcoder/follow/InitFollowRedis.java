package com.nowcoder.follow;

import com.nowcoder.WendaApplication;
import com.nowcoder.model.EntityType;
import com.nowcoder.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
/**
 * @program: wenda
 * @description: 初始化数据库中的数据
 * @author: Li Shuai
 * @create: 2019-01-20 13:45
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class InitFollowRedis {

    @Autowired
    FollowService followService;

    @Test
    public void contextLoads() {
        for (int followedUserId = 0; followedUserId < 30; followedUserId++) {
            for (int followerUserId = 0; followerUserId < followedUserId; followerUserId++) {
                followService.follow(followerUserId, EntityType.USER, followedUserId);
            }
        }

        for (int followedUserId = 0; followedUserId < 30; followedUserId++) {
            followService.unfollow(0, EntityType.USER, followedUserId);
        }

        for (int followedUserId = 15; followedUserId < 30; followedUserId++) {
            for (int followerUserId = 0; followerUserId < followedUserId; followerUserId++) {
                followService.unfollow(followerUserId, EntityType.USER, followedUserId);
            }
        }
    }
}
