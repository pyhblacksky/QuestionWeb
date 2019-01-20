package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
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
 * @Date: 2019/1/20 16:44
 * @Version 1.0
 * @Function:
 *      点赞的handler,即点赞事件的处理
 *      收到这个事件，开始进行相关处理
 *      收到这个点赞事件给用户发送消息
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    //收到这个点赞事件给用户发送消息
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);//收到系统发来的消息
        message.setToId(model.getEntityOwnerId());//实体拥有者
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户 " + user.getName() +
                " 赞了你的评论，http://127.0.0.1:8080/question/"+model.getExts("questionId"));//需要进行相关设置

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);//注册like事件
    }
}
