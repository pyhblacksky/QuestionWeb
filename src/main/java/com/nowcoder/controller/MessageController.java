package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/18 16:02
 * @Version 1.0
 * @Function:消息控制层，对外接口
 */
@Controller
public class MessageController {

    private final static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    //发消息是一个弹窗，所以用responseBody,使用Json的返回
    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try{
            //说明没有登录
            if(hostHolder.getUser() == null){
                return WendaUtil.getJSONString(999, "未登录");
            }

            User user = userService.selectByName(toName);
            if(user == null){
                return WendaUtil.getJSONString(1,"用户不存在");
            }

            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            messageService.addMessage(message);
            //Json返回0，表示成功
            return WendaUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("发送消息失败 " + e.getMessage());
            return WendaUtil.getJSONString(1, "发信失败");
        }
    }

    //获取消息列表
    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if(hostHolder.getUser() == null){
            return "redirect:/reglogin";    //当前用户为空，跳转登录
        }
        int localUserId = hostHolder.getUser().getId();//当前用户id
        List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
        List<ViewObject> conversations = new ArrayList<>();
        for(Message message : conversationList){
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            //获得对方是谁
            int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
            vo.set("user", userService.getUser(targetId));
            vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);//传入页面
        return "letter";
    }

    //获取消息详情页，点击了表示已读
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversationId") String conversationId){
        try {
            List<Message> messageList = messageService.getConversationDeatail(conversationId,0,10);
            //有复杂对象，如有用户头像等，使用ViewObject
            List<ViewObject> messages = new ArrayList<>();
            for(Message message : messageList){
                ViewObject vo = new ViewObject();
                messageService.updateRead(hostHolder.getUser().getId());//点击后，更新为读取状态
                vo.set("message", message);//取出消息
                vo.set("user", userService.getUser(message.getFromId()));//取出消息关联的用户
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e){
            logger.error("获取详细消息失败 " + e.getMessage());
        }
        return "letterDetail";
    }

}
