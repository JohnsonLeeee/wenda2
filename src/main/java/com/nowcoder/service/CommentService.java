package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    @Autowired
    MySensitiveService mySensitiveService;

    /**
     * 添加一条评论
     * @param comment comment类
     * @return 添加成功就返回comment的Id,添加失败返回0
     */
    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(mySensitiveService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    /**
     * 获取评论
     * @param entityId question/comment的id
     * @param entityType question/comment
     * @return 返回Comment的列表
     */

    public List<Comment> getCommentByEntity(int entityId, EntityType entityType) {
        switch (entityType) {
            case QUESTION: return commentDAO.selectByEntity(entityId, 1);
            case COMMENT:  return commentDAO.selectByEntity(entityId, 2);
        }
        return commentDAO.selectByEntity(entityId, 1);

    }

    public int getCommentCountByEntityId(int entityId, int entityType) {
        return commentDAO.getCommentCountByEntityId(entityId, entityType);
    }

    public int getCommentCountByUserId(int userId, int entityType) {
        // entitytype为question,表示回答数
        return commentDAO.getCommentCountByUserId(userId, entityType);
    }

    public int getAnswerCountByUserId(int userId) {
        return commentDAO.getCommentCountByUserId(userId, EntityType.QUESTION.getInt());
    }

    public boolean deleteComment(int commentId) {
        return commentDAO.updateComment(commentId, 1);
    }

    public Comment getCommentById(int id) {
        return commentDAO.getCommentById(id);
    }
}
