package com.nowcoder.util;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

/**
 * @Author: pyh
 * @Date: 2019/1/19 10:29
 * @Version 1.0
 * @Function: redis 的 java 版  jedis演示
 */
public class JedisAdapter {

    //测试打印函数
    public static void print(int index, Object obj){
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    public static void main(String[] args){

        Jedis jedis = new Jedis("redis://localhost:6379/9");//连接本机6379端口，第九个数据库表
        jedis.flushDB();//删除当前数据库表

        jedis.set("hello", "world");//存储 k - v数据
        print(1, jedis.get("hello"));
        jedis.rename("hello","newhello");//key重命名
        print(2,jedis.get("newhello"));
        jedis.setex("hello2",15,"world");//设置过期时间,时间一过就消失。应用：验证码无效时间、缓存等

        //增减数据
        jedis.set("pv","100");
        jedis.incr("pv");//pv 值 +1操作
        jedis.incrBy("pv", 5); //+5
        print(3, jedis.get("pv"));
        jedis.decrBy("pv",2); //-2
        print(4, jedis.get("pv"));
        print(5, jedis.keys("*"));

        //演示list
        String listName = "list";
        for(int i = 0; i < 10; i++){
            jedis.lpush(listName, "a"+String.valueOf(i));//list的push
        }

        print(6, jedis.lrange(listName, 4, 8));
        print(6, jedis.lrange(listName,0,3));//取出元素 0 - 4，左闭右闭区间
        print(7, jedis.llen(listName));
        print(8, jedis.lpop(listName));
        print(9, jedis.llen(listName));
        print(10, jedis.lindex(listName, 3));//直接取元素
        print(11, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));//插入某个之前
        print(12, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bb"));//插入某个之后
        print(13, jedis.lrange(listName,0, 100));

        //hash
        String userKey = "userxxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "123456");
        print(14, jedis.hget(userKey, "name"));
        print(15, jedis.hgetAll(userKey));
    }

}
