package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
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
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/question/{qid}"}, method = {RequestMethod.GET})
    public String questionDetail(@PathVariable("qid") int qid,
                                 Model model)  {
        Question question = questionService.selectQuestionById(qid);
        User loggedInUser = hostHolder.getUser();

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
                liked = likeService.getLikeStatus(loggedInUser.getId(), comment.getEntityTypeEnum(), comment.getEntityId());
            }
            long likeCount = likeService.getLikeCount(comment.getEntityTypeEnum(), comment.getEntityId());
            viewObject.set("liked", liked);
            viewObject.set("likeCount", likeCount);

            // 添加评论的评论数量
            int commentCount = commentService.getCommentCount(comment.getId(), EntityType.COMMENT.getAbbr());
            viewObject.set("commentCount", commentCount);

            vos.add(viewObject);
        }

        // 添加问题栏
        model.addAttribute("question", question);
        model.addAttribute("loggedInUser", loggedInUser);
        // 添加回答栏
        model.addAttribute("comments", vos);

        return "detail";
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
            logger.info("开始执行questionService.addQuestion");
            if (questionService.addQuestion(question) > 0) {
                logger.info("执行questionService.addQuestion结束");
                return WendaUtil.getJSONString(0);
            }
        } catch(Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }

        return WendaUtil.getJSONString(1, "添加问题失败");
    }
}
