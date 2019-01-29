package com.nowcoder.controller;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.nowcoder.util.WendaUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/question/{qid}"}, method = {RequestMethod.GET})
    public String questionDetail(@PathVariable("qid") int qid,
                                 Model model)  {
        // 获取问题内容
        Question question = questionService.getQuestionById(qid);
        User loggedInUser = hostHolder.getUser();

        // 判断是否关注
        boolean followed = false;
        if (loggedInUser != null) {
            followed = followService.isFollower(loggedInUser.getId(), EntityType.QUESTION, qid);
        }

        // 添加关注此问题的人
        List<Integer> followUserIds = followService.getFollowers(EntityType.QUESTION, qid, 0, 15);
        List<User> followUsers = new ArrayList<>(15);
        for (int id : followUserIds) {
            followUsers.add(userService.getUser(id));
        }


        List<Comment> comments = commentService.getCommentByEntity(question.getId(), EntityType.QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment: comments) {
            // 添加问题和user实体
            User commentUser = userService.getUser(comment.getUserId());
            ViewObject viewObject = new ViewObject();
            viewObject.set("comment", comment);
            viewObject.set("user", commentUser);

            // 添加点赞数量与状态
            int liked;
            if (loggedInUser == null) {
                liked = 0;
            } else {
                liked = likeService.getLikeStatus(loggedInUser.getId(), EntityType.COMMENT, comment.getId());
            }
            long likeCount = likeService.getLikeCountByEntityId(EntityType.COMMENT, comment.getId());
            // logger.info(String.valueOf(likeCount));
            viewObject.set("liked", liked);
            viewObject.set("likeCount", likeCount);

            // 添加评论的评论数量
            int commentCount = commentService.getCommentCountByEntityId(comment.getId(), EntityType.COMMENT.getAbbr());
            viewObject.set("commentCount", commentCount);

            vos.add(viewObject);
        }

        // 添加问题栏
        model.addAttribute("question", question);
        // model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("followed", followed);
        model.addAttribute("followUsers", followUsers);
        model.addAttribute("followCount", followService.getFollowerCount(EntityType.QUESTION, question.getId()));

        // 添加回答栏
        model.addAttribute("comments", vos);

        return "question";
    }

    @RequestMapping(path = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            // logger.info("开始执行questionService.addQuestion");
            if (questionService.addQuestion(question) > 0) {
                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                        .setActorId(question.getUserId())
                        .setCarrierEntityType(EntityType.QUESTION)
                        .setCarrierEntityId(question.getId())
                        .setCarrierEntityOwnerId(question.getUserId())
                        .setExts("title", question.getTitle())
                        .setExts("content", question.getContent())
                );
                // logger.info("执行questionService.addQuestion结束");
                return WendaUtil.getJSONString(0);
            }
        } catch(Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }

        return WendaUtil.getJSONString(1, "添加问题失败");
    }
}
