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

    public long like(int userId, EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }


    public int getLikeStatus(int userId, EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        } else if (jedisAdapter.sismember(disLikeKey, String.valueOf(userId))) {
            return -1;
        }
        return 0;
    }

    public long getLikeCount(EntityType entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);

        return jedisAdapter.scard(likeKey);
    }










}
