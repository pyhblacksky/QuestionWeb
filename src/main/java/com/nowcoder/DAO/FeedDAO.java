package com.nowcoder.DAO;

import com.nowcoder.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: pyh
 * @Date: 2019/1/23 10:52
 * @Version 1.0
 * @Function:
 *      Feed数据交互层，读取层
 *
 */
@Mapper
@Component("FeedDAO")
public interface FeedDAO {

    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, data, created_date, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert(value = {" insert into ", TABLE_NAME, " ( ", INSERT_FIELDS,
            ") values (#{userId}, #{data}, #{created_date}, #{type})"})
    int addFeed(Feed feed);

    //推的模式，通过id
    @Select(value = {"select ", SELECT_FIELDS," from ", TABLE_NAME, " where id = #{id}"})
    Feed selectById(int userId);

    //拉取获得信息。count是选取数。对于未登录用户，不需要参数userIds。用xml读取
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);

}
