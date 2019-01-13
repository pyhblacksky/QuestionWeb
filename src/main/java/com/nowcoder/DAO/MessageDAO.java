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
    String INSERT_FIELDS = " fromid, toid, content, conversation_id, created_date ";
    String SELECT_FIELDS = " id , " + INSERT_FIELDS;

    @Insert(value = {"insert into ", TABLE_NAME, "( ", INSERT_FIELDS,
            " ) values (#{fromid}, #{toid}, #{content}, #{conversationId}, #{createdDate})"})
    int addMessage(Message message);

    @Select(value = {" select ", SELECT_FIELDS ," from " , TABLE_NAME, " where id = #{id}"})
    Message getMessageById(int id);
}
