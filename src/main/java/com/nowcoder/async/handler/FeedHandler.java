package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author: pyh
 * @Date: 2019/1/23 11:25
 * @Version 1.0
 * @Function:
 *      对于评论新鲜事的事件处理
 *      如果有评论增加，进行新鲜事处理
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    //构建date
    private String buildFeedData(EventModel model){
        //map存储，json转换
        Map<String, String> map = new HashMap<>();
        //触发用户是通用的
        User actor = userService.getUser(model.getActorId());
        if(actor == null){
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        //如果发生评论，或关注问题(关注的实体是问题)，则执行
        if(model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW  && model.getEntityType() == EntityType.ENTITY_QUESTION)){
            Question question = questionService.selectById(model.getEntityId());
            if(question == null){
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            //返回Json对象
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        //随机产生用户id，仅仅用于测试
        Random r = new Random();
        model.setActorId(1+r.nextInt(10));
        ////////////////////////////////////////////

        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if(feed.getData() == null){
            return;//为空。不处理这个事件
        }

        feedService.addFeed(feed);//添加feed。拉的模式
    }

    @Override
    public List<EventType> getSupportEventType() {
        //当发生关注行为和评论行为时，都进行处理
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
