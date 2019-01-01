package com.nowcoder.service;

import com.nowcoder.model.EntityType;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.nowcoder.util.JedisAdapter;
import org.springframework.stereotype.Service;

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
    public long getLikeCount(EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);

        return jedisAdapter.sCard(likeKey);
    }










}
