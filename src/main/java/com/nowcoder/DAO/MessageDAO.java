package com.nowcoder.DAO;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "MessageDAO")
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, conversation_id, has_read, created_date ";
    String SELECT_FIELDS = " id , " + INSERT_FIELDS;

    @Insert(value = {"insert into ", TABLE_NAME, "( ", INSERT_FIELDS,
            " ) values (#{fromId}, #{toId}, #{content}, #{conversationId}, #{hasRead}, #{createdDate})"})
    int addMessage(Message message);

    @Select(value = {"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where conversation_id = #{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    //选取站内信列表数据
    @Select(value = {"select ",INSERT_FIELDS," , count(id) as id from ( select * from " ,
            TABLE_NAME," where from_id = #{userId} or to_id = #{userId} order by created_date desc) tt " +
            "group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    //获得未读消息
    @Select(value = {"select count(id) from ", TABLE_NAME,
            " where has_read = 0 and to_id = #{userId} and conversation_id = #{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);

    //更新读取的状态
    @Update(value = {"update ", TABLE_NAME, " set has_read = 1 where to_id = #{userId}"})
    void updateRead(@Param("userId") int userId);

    @Select(value = {" select ", SELECT_FIELDS ," from " , TABLE_NAME, " where id = #{id}"})
    Message selectMessageById(int id);
}
