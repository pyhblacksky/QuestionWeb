package com.nowcoder.service;

import com.nowcoder.DAO.FeedDAO;
import com.nowcoder.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/23 10:50
 * @Version 1.0
 * @Function:
 *      新鲜事，feed服务层
 *      读取feed
 */
@Service
public class FeedService {

    @Autowired
    FeedDAO feedDAO;

    //返回该用户关注的事务的最新动态。拉模式
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    //增加feed事件
    public boolean addFeed(Feed feed){
        feedDAO.addFeed(feed);
        return feed.getId() > 0;//添加成功，id>0
    }

    //推模式
    public Feed getById(int id){
        return feedDAO.selectById(id);
    }

}
