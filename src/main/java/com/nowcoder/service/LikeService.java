package com.nowcoder.service;

import com.nowcoder.model.EntityType;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    public long like(int userId, EntityType entityType, int entityId) {
        // 把userId加入到赞的列表里
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        // 从dislike中去除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, EntityType entityType, int entityId) {
        String DisLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(DisLikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        return jedisAdapter.scard(DisLikeKey);
    }

    /**
     * @param userId userId
     * @param entityType entityType
     * @param entityId entityId
     * @return 1， 点赞； 2， 点踩； 0， 无操作。
     */
    public int getLikeStatus(int userId, EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sIsMember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String DisLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        if (jedisAdapter.sIsMember(DisLikeKey, String.valueOf(userId))) {
            return -1;
        }
        return 0;
    }

    public long getLikeCount(EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }


}
