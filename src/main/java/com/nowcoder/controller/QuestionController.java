package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/15 20:32
 * @Version 1.0
 * @Function:
 *      实现提问控制
 *      及提问详情页的控制
 */
@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    //当前用户
    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

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
                //未登录，返回999,=>使登录
                return WendaUtil.getJSONString(999);
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

    //点击题目跳转详情页
    @RequestMapping(value = {"/question/{qid}"})
    public String questionDetail(Model model, @PathVariable("qid") int qid){
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));

        //增加评论显示
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        //同时获得评论的用户
        List<ViewObject> comments = new ArrayList<>();
        for(Comment comment : commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);

            //判断是否有当前用户
            if(hostHolder.getUser() == null){
                vo.set("liked", 0);
            } else{
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }

            //增加点赞显示
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));

            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }

        model.addAttribute("comments", comments);

        //增加关注用户者
        List<ViewObject> followUsers = new ArrayList<>();
        //获取用户关注信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users){
            ViewObject vo = new ViewObject();
            User user = userService.getUser(userId);
            if(user == null){
                continue;
            }
            vo.set("name", user.getName());
            vo.set("headUrl", user.getHeadUrl());
            vo.set("id", user.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if(hostHolder.getUser() != null){
            model.addAttribute("followed",
                    followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }

        return "detail";
    }
}
