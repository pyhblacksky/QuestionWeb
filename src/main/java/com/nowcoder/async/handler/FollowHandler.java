package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/21 20:39
 * @Version 1.0
 * @Function:
 *      关注的事件处理
 *      发送站内信。针对关注的问题和关注的是用户，处理方法不同
 */
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);//收到系统发来的消息
        message.setToId(model.getEntityOwnerId());//实体拥有者
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        //关注的是问题
        if(model.getEntityType() == EntityType.ENTITY_QUESTION){
            message.setContent("用户 " + user.getName() +
                    " 关注了你的问题，http://127.0.0.1:8080/question/"+model.getEntityId());//需要进行相关设置
        } else if(model.getEntityType() == EntityType.ENTITY_USER){
            //如果关注的是人
            message.setContent("用户 " + user.getName() +
                    " 关注了你，http://127.0.0.1:8080/user/"+model.getActorId());//需要进行相关设置
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
