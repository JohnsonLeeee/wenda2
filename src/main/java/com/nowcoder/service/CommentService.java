package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public List<Comment> getCommentByEntity(int entityId, EntityType entityType) {
        switch (entityType) {
            case QUESTION: return commentDAO.selectByEntity(entityId, 1);
            case COMMENT:  return commentDAO.selectByEntity(entityId, 2);
        }
        return commentDAO.selectByEntity(entityId, 1);

    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int commentId) {
        return commentDAO.updateComment(commentId, 1);
    }




}
