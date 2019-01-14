package com.nowcoder.DAO;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "MessageDAO")
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, conversation_id, has_read, created_date ";
    String SELECT_FIELDS = " id , " + INSERT_FIELDS;

    @Insert(value = {"insert into ", TABLE_NAME, "( ", INSERT_FIELDS,
            " ) values (#{fromId}, #{toId}, #{content}, #{conversationId}, #{hasRead}, #{createdDate})"})
    int addMessage(Message message);

    @Select(value = {" select ", SELECT_FIELDS ," from " , TABLE_NAME, " where id = #{id}"})
    Message selectMessageById(int id);
}
