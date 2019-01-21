package com.nowcoder.service;

import com.nowcoder.model.EntityType;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @program: wenda
 * @description: 关注/被关注服务
 * @author: Li Shuai
 * @create: 2019-01-06 09:30
 **/

@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 关注， user可关注QEUSTION, 也可以关注USER
     * @param userId 关注人
     * @param entityType 被关注的实体类型，QUESTION, COMMENT, USER
     * @param entityId 被关注实体的id
     * @return 成功返回true, 失败返回false
     */
    public boolean follow(int userId, EntityType entityType, int entityId) {
        final String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        final String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        // jedis事务,把关注和取关打包成一个事务
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);

        // 判断是否正确执行
        return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    /**
     * 取关
     * @param userId 关注人
     * @param entityType 被关注的实体类型，QUESTION, COMMENT, USER
     * @param entityId 被关注实体的id
     * @return 成功返回true, 失败返回false
     */
    public boolean unfollow(int userId, EntityType entityType, int entityId) {
        final String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        final String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followerKey, String.valueOf(userId));
        tx.zrem(followeeKey, String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    /**
     *  把set<String>类型转换为List<Integer>类型
     * @param idSet set类型的ID
     * @return ArrayList类型的id
     */
    private List<Integer> getIdsFromSet(Set<String> idSet) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idSet) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    public List<Integer> getFollowers(EntityType entityType, int entityId, int count) {
        final String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zRevRange(followerKey, 0, count));
    }

    /**
     * 关注我的人
     * @param entityType 被关注的实体类型，QUESTION, COMMENT, USER
     * @param entityId 被关注实体的id
     * @param offset 偏移量
     * @param count 数量
     * @return
     */
    public List<Integer> getFollowers(EntityType entityType, int entityId, int offset, int count) {
        final String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zRevRange(followerKey, offset, count));
    }

    public List<Integer> getFollowers(int entityId, EntityType entityType,  int offset, int count) {
        final String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zRevRange(followerKey, offset, count));
    }

    /**
     *  关注我的人数量/关注本问题的人的数量
     * @param entityType 被关注的实体类型，QUESTION, COMMENT, USER
     * @param entityId 被关注实体的id
     * @return long类型的粉丝/关注数量
     */
    public long getFollowerCount(EntityType entityType, int entityId) {
        final String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zCard(followerKey);
    }


    public List<Integer> getFollowees(int userId, EntityType entityType, int count) {
        final String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zRevRange(followeeKey, 0, count));
    }

    /**
     * 我关注的人/问题
     * @param userId 用户ID
     * @param entityType USER / QUESTION
     * @param count 数量，分页用
     * @param offset 偏移量
     * @return entityId的List
     */
    public List<Integer> getFollowees(int userId, EntityType entityType, int offset, int count) {
        final String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zRevRange(followeeKey, offset, count));
    }

    // 我关注的人的数量
    public long getFolloweeCount(int userId, EntityType entityType) {
        final String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zCard(followeeKey);
    }

    // 是否是粉丝
    public boolean isFollower(int userId, EntityType entityType, int entityId) {
        final String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zScore(followerKey, String.valueOf(userId)) != null;
    }
}
