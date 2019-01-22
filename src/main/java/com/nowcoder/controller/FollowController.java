package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: wenda
 * @description: FollowController
 * @author: Li Shuai
 * @create: 2019-01-07 22:40
 **/
@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);
    @Autowired
    EventProducer eventProducer;

    @Autowired
    FollowService followService;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    // todo 关注某个人
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int followeeUserId) {
        if (hostHolder == null) {
            // 999重新登录
            return WendaUtil.getJSONString(999);
        }

        boolean result = followService.follow(hostHolder.getUser().getId(), EntityType.USER, followeeUserId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setCarrierEntityId(followeeUserId)
                .setCarrierEntityType(EntityType.USER)
                .setCarrierEntityOwnerId(followeeUserId)
        );

        // 成功返回0， 失败返回1; 总共关注的人数
        return WendaUtil.getJSONString(result ? 0 : 1,
                String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.USER)));
    }


    // 取关
    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int followeeUserId) {
        logger.info("开始执行/unfollowUser");
        if (hostHolder == null) {
            return WendaUtil.getJSONString(999);
        }

        boolean result = followService.unfollow(hostHolder.getUser().getId(), EntityType.USER, followeeUserId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setCarrierEntityId(followeeUserId)
                .setCarrierEntityType(EntityType.USER)
                .setCarrierEntityOwnerId(followeeUserId)
        );

        // 成功返回0， 失败返回1; 总共关注的人数
        return WendaUtil.getJSONString(result ? 0 : 1,
                String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.USER)));
    }

    // todo 关注某个问题
    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        // logger.info("开始执行/followQuestion");
        if (hostHolder == null) {
            return WendaUtil.getJSONString(999);
        }

        // 防止用户直接调用底层代码，导致程序报错
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }

        boolean result = followService.follow(hostHolder.getUser().getId(), EntityType.QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setCarrierEntityId(questionId)
                .setCarrierEntityType(EntityType.QUESTION)
                .setCarrierEntityOwnerId(question.getUserId())
        );

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.QUESTION, questionId));

        // 成功返回0， 失败返回1; 然后把信息的Map传给前端
        logger.info(WendaUtil.getJSONString(result ? 0 : 1, info));
        return WendaUtil.getJSONString(result ? 0 : 1, info);
    }

    // todo 取关某个问题
    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        // logger.info("开始执行/unfollowQuestion");
        if (hostHolder == null) {
            return WendaUtil.getJSONString(999);
        }

        // 防止用户直接调用底层代码，导致程序报错
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }

        boolean result = followService.unfollow(hostHolder.getUser().getId(), EntityType.QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setCarrierEntityId(questionId)
                .setCarrierEntityType(EntityType.QUESTION)
                .setCarrierEntityOwnerId(question.getUserId())
        );

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.QUESTION, questionId));

        // 成功返回0， 失败返回1; 然后把信息的Map传给前端
        return WendaUtil.getJSONString(result ? 0 : 1, info);
    }

    // 我关注的人列表
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        model.addAttribute("curUser", userService.getUser(userId));
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.USER));

        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.USER, 0,10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        return "followees";
    }

    // 关注我的人列表
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        model.addAttribute("curUser", userService.getUser(userId));
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.USER, userId));

        List<Integer> followerIds = followService.getFollowers(EntityType.USER, userId,0,10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        return "followees_2";
    }


    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer userId : userIds) {
            User user = userService.getUser(userId);
            if (user == null) {
                continue;
            }

            ViewObject vo = new ViewObject();
            vo.set("user", user);
            // vo.set("commentCount", )
            vo.set("followerCount", followService.getFollowerCount(EntityType.USER, userId));
            vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.USER));
            vo.set("commentCount", commentService.getAnswerCountByUserId(userId));
            vo.set("likeCount", likeService.getAllAnswerLikeCountByUserId(userId));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.USER, userId));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }


}
