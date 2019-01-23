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
    private static String BIZ_LIKE = "LIKE";//点赞
    private static String BIZ_DISLKE = "DISLIKE";//点踩
    private static String BIZ_EVENETQUEUE = "EVENT_QUEUE";//事件

    private static String BIZ_FOLLOWER = "FOLLOWER";//粉丝
    private static String BIZ_FOLLOWEE = "FOLLOWEE";//关注对象
    private static String BIZ_TIMELINE = "TIMELINE";//时间轴

    //点赞存储的key
    public static  String getLikeKey(int entityType, int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    //点踩存储的key
    public static String getDislikeKey(int entityType, int entityId){
        return BIZ_DISLKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    //异步事件存储的key
    public static String getEventQueueKey(){
        return BIZ_EVENETQUEUE;
    }

    //获取粉丝的key,根据实体类型和id确定
    public static String getFollwerKey(int entityType, int entityId){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    //获取关注对象的key
    public static String getFollweeKey(int userId, int entityType){
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    //获取时间轴的key
    public static String getTimelineKey(int userId){
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
}
