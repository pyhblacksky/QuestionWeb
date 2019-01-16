package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: pyh
 * @Date: 2019/1/15 20:32
 * @Version 1.0
 * @Function:实现提问控制
 */
@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    //当前用户
    @Autowired
    HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    //AJX
    @RequestMapping(value = {"/question/add"}, method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser() == null){
                //未登录，返回匿名用户
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            } else{
                //当前用户id
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){
                //说明添加成功,返回0
                return WendaUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("增加问题失败 " + e.getMessage());
        }
        //错误，返回1
        return WendaUtil.getJSONString(1, "失败");
    }

}
