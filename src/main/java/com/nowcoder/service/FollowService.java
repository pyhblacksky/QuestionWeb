package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: pyh
 * @Date: 2019/1/21 10:58
 * @Version 1.0
 * @Function:
 *      关注功能的服务。
 *      Follower：粉丝、关注者
 *      Followee:被关注对象
 */
@Service
public class FollowService {

    private final static Logger logger = LoggerFactory.getLogger(FollowService.class);

    @Autowired
    JedisAdapter jedisAdapter;

    //关注功能.用户id，关注的实体和实体id
    public boolean follow(int userId, int entityType, int entityId){
        //放入关注列表，放入实体列表
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFollweeKey(userId, entityType);
        Date date = new Date();

        //获取一个redis
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);//获取事务.事务的操作，整个过程要么同时成功，要么同时失败
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));//添加粉丝
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));//添加关注对象
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;

    }

    //取消关注功能
    public boolean unfollow(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFollweeKey(userId, entityType);
        Date date = new Date();

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followerKey, String.valueOf(userId));
        tx.zrem(followeeKey, String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    //获取所有的粉丝，每一个实体都有关粉丝
    public List<Integer> getFollowers(int entityType, int entityId, int count){
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, 0, count));
    }

    //获取所有的粉丝，offset翻页用的。
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count){
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, offset, count));
    }

    //获取所有的关注者（关注对象），每一个实体都有关注者.反向选取，获取最新的
    public List<Integer> getFollowees(int userId, int entityType, int count){
        String followeeKey = RedisKeyUtil.getFollweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    //获取所有的关注者（关注对象），offset翻页用的。
    public List<Integer> getFollowees(int userId, int entityType, int offset, int count){
        String followeeKey = RedisKeyUtil.getFollweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, count));
    }

    //转换函数,set集合转化为List
    private List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> ids = new ArrayList<>();
        for(String str : idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    //获取粉丝数量
    public long getFollowerCount(int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    //获取关注者数量
    public long getFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFollweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    //判断是否关注。取出分数，此处在redis中分数是根据时间来确定的
    public boolean isFollower(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollwerKey(entityType, entityId);
        //分数不为空，表示存在，即关注
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
