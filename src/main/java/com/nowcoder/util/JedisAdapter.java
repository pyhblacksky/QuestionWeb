package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * @Author: pyh
 * @Date: 2019/1/19 10:29
 * @Version 1.0
 * @Function:
 *      1. Main函数中 redis 的 java 版  jedis演示
 *      2. 实现redis 对外功能，使用set实现点赞点踩功能
 */
@Service
public class JedisAdapter implements InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    //redis连接池
    private JedisPool pool;

    //测试打印函数
    public static void print(int index, Object obj){
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    //redis功能测试
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

        //hash   即 k-v结构中的k-v。增减特殊字段好用
        String userKey = "userxxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "123456");
        print(14, jedis.hget(userKey, "name"));
        print(15, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "age"); //删除k-v字段
        print(15, jedis.hgetAll(userKey));
        print(16, jedis.hexists(userKey, "email"));//判断是否存在某个字段
        print(17, jedis.hexists(userKey, "phone"));
        print(18, jedis.hkeys(userKey));    //取出keys
        print(19, jedis.hvals(userKey));    //取出val
        jedis.hsetnx(userKey , "school", "hju");//不存在才会设置,防止重复写入
        print(20, jedis.hgetAll(userKey));
        jedis.hsetnx(userKey, "name", "qq");
        print(20, jedis.hgetAll(userKey));

        //set，集合，去重，可以实现啊点赞功能
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++){
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*i));
        }
        print(20, jedis.smembers(likeKey1));
        print(20, jedis.smembers(likeKey2));
        print(21, jedis.sunion(likeKey1,likeKey2));//求并集
        print(22, jedis.sdiff(likeKey1,likeKey2));//求集合1有，集合2没有
        print(23, jedis.sinter(likeKey1, likeKey2));//求交集
        print(25, jedis.sismember(likeKey1, "12"));//查询集合中是否含有某个数
        jedis.srem(likeKey1, "5");  //删除操作
        print(26, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25");//从集合1向集合2移动一个指定元素
        print(27, jedis.smembers(likeKey1));
        print(27, jedis.smembers(likeKey2));
        print(28, jedis.scard(likeKey1));//计算该集合有多少值，即个数
        print(29, jedis.srandmember(likeKey1, 3));//随机取值

        //优先队列，可用于排行榜。sorted set  zset.带分值的
        String rank = "rankKey";
        jedis.zadd(rank, 20, "jim");
        jedis.zadd(rank, 24, "qk");
        jedis.zadd(rank, 11, "ds");
        jedis.zadd(rank, 80, "zx");
        jedis.zadd(rank, 70, "cv");
        jedis.zadd(rank, 95, "bn");
        jedis.zadd(rank, 23, "jk");
        jedis.zadd(rank, 2, "kio");
        jedis.zadd(rank, 55, "pou");
        print(30, jedis.zcard(rank));//求总数
        print(31, jedis.zcount(rank, 50, 80));//求某个区间内的个数
        print(32 ,jedis.zscore(rank, "zx"));//查询某个key的分数
        jedis.zincrby(rank, 5, "zx");
        print(33 ,jedis.zscore(rank, "zx"));
        jedis.zincrby(rank, 17, "hsad");//不存在，则新增
        print(34, jedis.zrange(rank, 0, 100));//选出排序从a-b名次的值。默认从小到大排序
        print(35, jedis.zrange(rank, 0,5));
        print(35, jedis.zrevrange(rank, 0,5));//逆序求，即从大到小
        //遍历某个分数区间的值
        for(Tuple tuple : jedis.zrangeByScoreWithScores(rank, "20", "90")){
            print(37, tuple.getElement() + " : " + String.valueOf(tuple.getScore()));//k - v结结构
        }

        print(38, jedis.zrevrank(rank , "zx"));//查看排名，rev：从大到小。包含第0名

        //所有人分值一样时,根据字典序排序
        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");
        jedis.zadd(setKey, 1, "f");
        print(40, jedis.zlexcount(setKey, "-", "+"));// "-" 表示负无穷， "+"正无穷，计算有多少个
        print(41, jedis.zlexcount(setKey, "[b", "[d"));//包含两个边界[，闭区间
        print(41, jedis.zlexcount(setKey, "(b", "[d"));//不包含b(，开区间
        print(42, jedis.zrange(setKey, 0 , 10));
        jedis.zremrangeByLex(setKey, "(c", "+");//根据字典序删除
        print(43, jedis.zrange(setKey, 0, 10));

        print(44, jedis.get("pv"));
        //连接池
        JedisPool jedisPool = new JedisPool("redis://localhost:6379/9");//必须制定端口和数据库
        for (int i = 0; i < 10; i++){
            Jedis j = jedisPool.getResource();
            print(45, j.get("pv"));
            j.close();//用完必须还回，要不然被独占
            //应用：网页请求时，用完还回
        }

        //redis作缓存
        User user1 = new User();
        user1.setName("opq");
        user1.setPassword("123");
        user1.setHeadUrl("a.png");
        user1.setSalt("salt");
        user1.setId(7);
        jedis.set("user1", JSONObject.toJSONString(user1));//通过Json String的形式存储入redis数据库。对象序列号存储
        print(46, jedis.get("user1"));

        //通过Json取出，直接赋值给某个对象
        User user2 = JSON.parseObject(jedis.get("user1"), User.class);  //json序列化实现缓存
        print(47, user2);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //pool进行初始化
        pool = new JedisPool("redis://localhost:6379/10");
    }

    //集合对外的增加操作
    public long sadd(String key, String val){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key, val);
        } catch (Exception e){
            logger.error("发生异常 ： " + e.getMessage());
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //删除功能
    public long srem(String key, String val){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, val);
        } catch (Exception e){
            logger.error("发生异常 ： "+ e.getMessage());
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //计数功能
    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e){
            logger.error("发生错误 ：" + e.getMessage());
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //是否存在某个成员
    public boolean sismember(String key, String val){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key ,val);
        } catch (Exception e){
            logger.error("发生错误 ： "+ e.getMessage());
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    //redis 的 list_push     list是队列
    public long lpush(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e){
            logger.error("发生异常 ：" + e.getMessage());
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }
}
