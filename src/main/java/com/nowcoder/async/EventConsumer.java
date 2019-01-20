package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: pyh
 * @Date: 2019/1/20 11:21
 * @Version 1.0
 * @Function:
 *      异步处理框架之
 *          取出队列中的事件
 *          处理队列中所有event,
 *          把handler和event之间的关系建立
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    //取出event， 查看其type，进行相应处理一批handler。Map实现消息分发
    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    //ApplicationContext知道上下文，知道当前有多少个Beans
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //找出所有的event工程实现类。找出实现这个接口的类。实现EventHandler的类自动注册。扩展性很强
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventType();//该event关注的事件

                for(EventType type : eventTypes){
                    //第一次注册，不包含，注册
                    if(!config.containsKey(type)){
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());//加入handler
                }
            }
        }

        //多线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    //events没有取到事件，一直阻塞。0表示一直阻塞
                    List<String> events = jedisAdapter.brpop(0, key);
                    for(String message : events){
                        //第一个返回值是key，先过滤。因为redis中是这样保存的。剩下的是model
                        if (message.equals(key)){
                            continue;
                        }

                        //将Json反序列化
                        //如果不进行处理(不包含)，则说明是非法事件。跳过
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        //寻找handler进行处理,寻找链式
                        for(EventHandler handler : config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();//开启线程
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;//存储接口
    }
}
