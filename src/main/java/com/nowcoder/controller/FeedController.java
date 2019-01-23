package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: wenda
 * @description: feed流
 * @author: Li Shuai
 * @create: 2019-01-22 22:03
 **/
@Controller
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET})
    public String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<Integer> followeeIds = followService.getFollowees(localUserId, EntityType.USER, Integer.MAX_VALUE);
        List<Feed> feeds = new ArrayList<>(20);

        if (localUserId != 0) {
            feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followeeIds, 20);
        }
        model.addAttribute("feeds", feeds);

        return "feeds";
    }

    /*
    pull和push区别，假设A关注B，B发动态后，push到A的timelineRedis中；
    当A不再关注B，A的pullfeeds将不再显示B的动态；
    而pushfeeds仍然显示B的刚才那条动态，因为已经push进入A的timeline了。
     */

    // todo : unfollow后，把相关feed从redis删除
    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET})
    public String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<String> feedIds = jedisAdapter.lRange(RedisKeyUtil.getTimelineKey(localUserId), 0, 20);
        List<Feed> feeds = new ArrayList<>(20);
        for (String id : feedIds) {
            Feed feed = feedService.getFeedById(Integer.parseInt(id));
            if (feed == null) {
                continue;
            }
            feeds.add(feed);
        }
        model.addAttribute("feeds", feeds);

        return "feeds";
    }
}
