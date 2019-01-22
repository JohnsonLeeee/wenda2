package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.nowcoder.util.WendaUtil;

import java.util.Date;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(WendaUtil.ANONYMOUS_USER_ID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);

            // 更新question表里的comment_count
            int count = commentService.getCommentCountByEntityId(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);

            // 将回答问题事件放入event队列
            eventProducer.fireEvent(new EventModel(EventType.ANSWER_QUESTION)
                    .setActorId(hostHolder.getUser().getId())
                    .setCarrierEntityId(questionId)
                    .setCarrierEntityType(EntityType.QUESTION)
                    .setCarrierEntityOwnerId(questionService.getQuestionById(questionId).getUserId())
            );
        } catch (Exception e) {
            logger.error("增加论评失败" + e.getMessage());
            e.printStackTrace();
        }


        return "redirect:/question/" + questionId;
    }

}
