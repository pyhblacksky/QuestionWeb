package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: pyh
 * @Date: 2019/1/21 16:02
 * @Version 1.0
 * @Function:
 *      关注功能的控制层
 *
 */
@Controller
public class FollowController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;

    //关注某个用户
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            //未登录，返回请求登录界面
            return WendaUtil.getJSONString(999);
        }

        //关注功能
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        //事件投递
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityType(EntityType.ENTITY_USER)
                .setEntityOwnerId(userId));
        //关注的入口
        return WendaUtil.getJSONString(ret ? 0 : 1,
                String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    //取消关注某个用户
    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId){
        if (hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0 : 1,
                String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    //关注问题
    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }

        //先判断问题存在不存在
        Question q = questionService.selectById(questionId);
        if(q == null){
            return WendaUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwnerId(q.getUserId()));

        //传入到前端,下面的关注小图标和其超链接
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        return WendaUtil.getJSONString( ret ? 0 : 1, info);
    }

    //取关问题
    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);
        }

        Question q = questionService.selectById(questionId);
        if(q == null){
            return WendaUtil.getJSONString(1 ,"问题不存在");
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId)
                .setEntityOwnerId(q.getUserId()));

        //传入到前端,下面的关注小图标和其超链接
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        return WendaUtil.getJSONString( ret ? 0 : 1, info);
    }

    //followees页面跳转。显示对应用户所关注的所有人
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid")int userId){
        //取出10个所关注的对象
        List<Integer> followeesIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0 ,10);
        //如果是登录用户
        if(hostHolder.getUser() != null){
            //登录用户执行逻辑
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeesIds));
        } else{
            //未登录用户执行逻辑
            model.addAttribute("followees", getUsersInfo(0, followeesIds));
        }
        return "followees";
    }

    //followers页面跳转。显示对应用户的粉丝
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId){
        //取出10个关注该用户的粉丝
        List<Integer> followerIds = followService.getFollowers(userId, EntityType.ENTITY_USER, 0, 10);

        if(hostHolder.getUser() != null){
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0,followerIds));
        }
        return "followers";
    }

    //页面显示公共函数.取出数据
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds){
        List<ViewObject> userInfos = new ArrayList<>();
        for(Integer uid : userIds){
            //判断用户是否存在
            User user = userService.getUser(uid);
            if(user == null){
                continue;
            }

            ViewObject vo = new ViewObject();
            vo.set("user", user);
            //vo.set("commentCount", commentService.getCommentCount());//获取用户评论数量
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));//获取用户粉丝的数量
            vo.set("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, uid));//获取用户关注对象的数量
            //判断是否登录
            if(localUserId != 0){
                //不为0，表示已登录.取出是否关注
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
