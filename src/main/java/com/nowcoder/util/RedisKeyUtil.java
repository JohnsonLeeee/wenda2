package com.nowcoder.util;

import com.nowcoder.model.EntityType;

/**
 * @program: wenda
 * @description: 根据不同的question/comment生成不同的RedisKey,防止随意生成，以致于原先的Key被覆盖。
 * @author: Li Shuai
 * @create: 2019-01-01 13:38
 **/

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT_QUEUE = "EVENT_QUEUE";


    public static String getLikeKey(EntityType entityType, int entityId) {
        return BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getDisLikeKey(EntityType entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getEventQueueKey() {
        return BIZ_EVENT_QUEUE;
    }

}
