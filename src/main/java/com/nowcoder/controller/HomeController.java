package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/"}, method = {RequestMethod.GET})
    public String home(Model model) {
        List<ViewObject> vos = getQuestion(0,0,10);
        model.addAttribute("vos", vos);
        return "index";
    }

    @RequestMapping(path = {"user/{userId}"}, method = {RequestMethod.GET})
    public String user(Model model, @PathVariable("userId") int userId) {
        // 个人主页详情
        ViewObject viewObject = new ViewObject();
        viewObject.set("user", userService.getUser(userId));
        viewObject.set("followerCount", followService.getFollowerCount(EntityType.USER, userId));
        viewObject.set("followeeCount", followService.getFolloweeCount(userId, EntityType.USER));
        viewObject.set("answerCount", commentService.getAnswerCountByUserId(userId));
        viewObject.set("likeCount", likeService.getAllAnswerLikeCountByUserId(userId));
        viewObject.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.USER, userId));
        model.addAttribute("profileUser", viewObject);

        // 最新动态
        List<ViewObject> vos = getQuestion(userId, 0, 10);
        model.addAttribute("vos", vos);

        return "profile";
    }

    private List<ViewObject> getQuestion(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vo.set("followCount", followService.getFollowerCount(EntityType.QUESTION, userId));
            vos.add(vo);
        }
        return vos;
    }
}
