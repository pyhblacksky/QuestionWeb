package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: pyh
 * @Date: 2019/1/19 16:38
 * @Version 1.0
 * @Function:
 *      数据存放在redis中，这里实现点赞点踩系统服务层
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    //默认的，返回某个类有多少人点赞，对外显示的
    public long getLikeCount(int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

    //表示某个用户对当前的赞和踩的状态，高亮的部分为其当前状态
    public int getLikeStatus(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);

        //喜欢，返回状态1
        if(jedisAdapter.sismember(likeKey ,String.valueOf(userId))){
            return 1;
        }

        //不喜欢，返回状态-1
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        if(jedisAdapter.sismember(dislikeKey, String.valueOf(userId))){
            return -1;
        }

        //没有操作，返回0
        return 0;
    }

    //点赞功能, 返回点赞数
    public long like(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        //不可能同时拥有赞和踩，如果赞了，踩就删除
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    //点踩功能，返回点赞数
    public long dislike(int userId, int entityType, int entityId){
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));

        //点赞减少
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

}
