package com.nowcoder.async;

import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/20 11:16
 * @Version 1.0
 * @Function:
 *      异步处理框架之
 *          专门用来处理event,分类
 *          记录事件映射
 */
public interface EventHandler {
    //处理event,当关注的event发生时，进行处理
    void doHandle(EventModel eventModel);

    //自己关注的是哪些eventType，注册自己
    List<EventType> getSupportEventType();

}
