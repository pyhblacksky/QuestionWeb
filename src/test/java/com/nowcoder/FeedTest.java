package com.nowcoder;

import com.nowcoder.DAO.FeedDAO;
import com.nowcoder.model.Feed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Author: pyh
 * @Date: 2019/1/23 14:52
 * @Version 1.0
 * @Function:
 *      feed数据库测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedTest {

    @Autowired
    FeedDAO feedDAO;

    @Test
    public void test(){

        for(int i = 0; i < 10; i++){
            Feed feed = new Feed();
            feed.setUserId(i+1);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * i);
            feed.setCreatedDate(date);
            feed.setType(1);
            feed.setData(String.format("Feed%d", i+1));

            feedDAO.addFeed(feed);
        }

    }

}
