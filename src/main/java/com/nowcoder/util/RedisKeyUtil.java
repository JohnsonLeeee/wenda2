package com.nowcoder.util;

import com.nowcoder.model.EntityType;

/**
 * @program: wenda
 * @description: 生成Redis名称
 * @author: Li Shuai
 * @create: 2018-12-29 18:20
 **/

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";

    public static String getLikeKey(EntityType entityType, int entityId) {
        return BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getDisLikeKey(EntityType entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + entityType + SPLIT + String.valueOf(entityId);
    }

}
