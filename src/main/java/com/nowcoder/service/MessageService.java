package com.nowcoder.service;

import com.nowcoder.DAO.MessageDAO;
import com.nowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/18 15:56
 * @Version 1.0
 * @Function:   关于消息的一个服务
 */
@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    //敏感词过滤
    @Autowired
    SensitiveService sensitiveService;

    //新增消息
    public int addMessage(Message message){
        //过滤
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? message.getId() : 0;
    }

    //取出批量消息
    public List<Message> getConversationDeatail(String conversationDetail, int offset, int limit){
        return messageDAO.getConversationDetail(conversationDetail, offset, limit);
    }

    //取出消息列表
    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    //获取未读消息数
    public int getConversationUnreadCount(int userId, String conversationId){
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }

    //更新消息的读取状态
    public void updateRead(int userId){
        messageDAO.updateRead(userId);
    }
}
