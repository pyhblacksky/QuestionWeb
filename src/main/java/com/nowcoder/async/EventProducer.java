package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author: pyh
 * @Date: 2019/1/20 10:21
 * @Version 1.0
 * @Function:
 *      异步处理框架之
 *          事件的入口，统一发送事件
 *          将事件送入队列
 */
@Service
public class EventProducer {

    private final static Logger logger = LoggerFactory.getLogger(EventProducer.class);

    //使用redis实现保存队列
    @Autowired
    JedisAdapter jedisAdapter;

    //将eventModel发送出去，保存入队列
    public boolean fireEvent(EventModel eventModel){
        try {
            //可以使用blockingqueue
            //BlockingQueue<EventType> queue = new ArrayBlockingQueue<>(10);
            //但在这为提高性能，采用redis,序列化存储
            //转化为json字符串
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);//放入队列
            return true;
        } catch (Exception e){
            logger.error("发送事件入队列失败 ： " +  e.getMessage());
            return false;
        }
    }
}
