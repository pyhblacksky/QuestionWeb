package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: pyh
 * @Date: 2019/1/21 8:17
 * @Version 1.0
 * @Function:
 *      登录异常处理
 *      实现登录判断，如果登录异常，发送邮件提醒
 *      与服务器的网络交互，必须设置为异步，因为速度较慢，不可能等待其卡死
 */
@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        //xxx判断这个用户登录异常
        //传入的key要与来源对应

        //测试时，不用考虑登录异常条件
        /*
        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExts("username"));
        mailSender.sendWithHTMLTemplate(model.getExts("email"),
                "登录IP异常", "mails/login_exception.html", map);
                */
    }

    @Override
    public List<EventType> getSupportEventType() {
        //只关心login这个事件
        return Arrays.asList(EventType.LOGIN);
    }
}
