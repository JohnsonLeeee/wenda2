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
            User commentUser = userService.getUser(comment.getUserId());
            ViewObject viewObject = new ViewObject();
            viewObject.set("comment", comment);
            viewObject.set("commentUser", commentUser);

            viewObject.set("likeCount", likeService.getLikeCount(comment.getEntityTypeByEnum(), comment.getEntityId()));
            if (hostHolder.getUser() == null) {
                viewObject.set("liked", 0);
            } else {
                viewObject.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), comment.getEntityTypeByEnum(), comment.getEntityId()));
            }
            vos.add(viewObject);
        }

        model.addAttribute("question", question);
        model.addAttribute("loggedInUser", loggedInUser);
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
