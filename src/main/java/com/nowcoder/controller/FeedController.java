package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/23 15:58
 * @Version 1.0
 * @Function:
 *      feed的控制层，跳转等事件
 */
@Controller
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    //用户主动拉的模式，进行消息推送
    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getPullFeeds(Model model){
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();

        //先查询关注了几个人
        List<Integer> followees = new ArrayList<>();
        //此时等不等于0，为登录状态
        if(localUserId != 0){
            //取出所有关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        //取出10个
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

}
