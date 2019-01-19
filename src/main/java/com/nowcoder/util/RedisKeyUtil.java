package com.nowcoder.util;

/**
 * @Author: pyh
 * @Date: 2019/1/19 16:41
 * @Version 1.0
 * @Function:
 *      RedisKey生成工具，为了保证RedisKey不重复
 */
public class RedisKeyUtil {

    private static String SPLIT = ":";//分隔符
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLKE = "DISLIKE";

    public static  String getLikeKey(int entityType, int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId){
        return BIZ_DISLKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }


}
