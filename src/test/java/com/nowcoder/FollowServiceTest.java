package com.nowcoder;

import com.nowcoder.model.EntityType;
import com.nowcoder.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * @Author: pyh
 * @Date: 2019/1/21 15:33
 * @Version 1.0
 * @Function:
 *      followService 数据 测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FollowServiceTest {

    @Autowired
    FollowService followService;


    @Test
    public void testFollow(){
        Random random = new Random();
        for(int i= 0; i < 11; i++){
            //互相关注
            for(int j = 1; j < i; ++j){
                followService.follow(j, EntityType.ENTITY_USER, i);
            }
        }
    }
}
