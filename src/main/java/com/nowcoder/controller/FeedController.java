package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
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
}
