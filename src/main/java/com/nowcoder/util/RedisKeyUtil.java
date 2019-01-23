package com.nowcoder.util;

import com.nowcoder.model.EntityType;

/**
 * @program: wenda
 * @description: 根据不同的question/comment生成不同的RedisKey,防止随意生成，以致于原先的Key被覆盖。
 * @author: Li Shuai
 * @create: 2019-01-01 13:38
 **/

public class RedisKeyUtil {
    // final修饰的属性表明是一个常数（创建后不能被修改）。
    private static final String SPLIT = ":";
    private static final String BIZ_LIKE = "LIKE";
    private static final String BIZ_DISLIKE = "DISLIKE";
    private static final String BIZ_EVENT_QUEUE = "EVENT_QUEUE";

    // 粉丝
    private static final String BIZ_FOLLOWER = "FOLLOWER";
    // 被关注对象
    private static final String BIZ_FOLLOWEE = "FOLLOWEE";

    private static final String BIZ_TIMELINE = "TIMELINE";


    /**
     *
     * @param entityType 被点赞的实体的类型，目前必须是comment; QUESTION和USER不可被点赞
     * @param entityId comment的id
     * @return Redis中存储点赞的set的键
     */
    public static String getLikeKey(EntityType entityType, int entityId) {
        // 这里注意BIZ_LIKE是否加final的区别，如果不加final，在这里可更改，加上final后，就不可更改了。
//        BIZ_LIKE = "LIKE";
//        BIZ_DISLIKE = "DISLIKE";
        return BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getDisLikeKey(EntityType entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getEventQueueKey() {
        return BIZ_EVENT_QUEUE;
    }

    public static String getFollowerKey(EntityType entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getFolloweeKey(int userId, EntityType entityType) {
        return BIZ_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + userId;
    }

}
