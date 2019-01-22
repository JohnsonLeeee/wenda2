package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.nowcoder.util.JedisAdapter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    CommentDAO commentDAO;

    /**
     *
     * @param userId 登录点赞用户的userId
     * @param entityType entityType
     * @param entityId entityId
     * @return 返回comment的赞数
     */
    public long like(int userId, EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sAdd(likeKey, String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sRem(disLikeKey, String.valueOf(userId));

        return jedisAdapter.sCard(likeKey);
    }

    /**
     *
     * @param userId 登录用户的userId
     * @param entityType entityType
     * @param entityId entityId
     * @return 返回comment的赞数
     */
    public long disLike(int userId, EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sRem(likeKey, String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sAdd(disLikeKey, String.valueOf(userId));

        return jedisAdapter.sCard(likeKey);
    }

    /**
     * @param userId userId
     * @param entityType entityType
     * @param entityId entityId
     * @return 1， 点赞； 2， 点踩； 0， 无操作。
     */
    public int getLikeStatus(int userId, EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        if (jedisAdapter.sIsMember(likeKey, String.valueOf(userId))) {
            return 1;
        } else if (jedisAdapter.sIsMember(disLikeKey, String.valueOf(userId))) {
            return -1;
        }
        return 0;
    }

    /**
     *
     * @param entityType entityType
     * @param entityId entityId
     * @return 返回comment的赞数
     */
    public long getLikeCountByEntityId(EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        // System.out.println(jedisAdapter.sCard(likeKey) + "likeKey=" + likeKey);
        return jedisAdapter.sCard(likeKey);
    }

    public long getAllAnswerLikeCountByUserId(int userId) {
        // 1. 获取userId所回答的所有的answer的Id, 不包括回答的评论的Id
        // answer即comment对应的entityType为Question,
        // 意为每个问题下的评论，即回答；与之相对应的还有回答的评论，评论的评论
        List<Integer> commentIds = new ArrayList<>();
        commentIds = commentDAO.getCommentIdsByUserId(userId, EntityType.QUESTION.getInt());
        // 2. 把所有的answerId的总数加起来
        long userLikeCount = 0;
        for (int commentId : commentIds) {
            // 这里的entityType表示被点赞的实体是一个COMMENT，并且必须是commment,
            // 因为目前question和user不可被点赞
            String LikeKey = RedisKeyUtil.getLikeKey(EntityType.COMMENT, commentId);
            long commentLikeCount = jedisAdapter.sCard(LikeKey);
            userLikeCount += commentLikeCount;
        }
        return userLikeCount;
    }











}
