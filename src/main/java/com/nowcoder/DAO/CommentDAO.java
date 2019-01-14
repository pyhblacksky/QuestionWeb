package com.nowcoder.DAO;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "CommentDAO")
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, created_date, entity_id, entity_type ";
    String SELECT_FIELDS = " id , " + INSERT_FIELDS;
    //注意区分什么时候是下划线_命名法，什么时候是驼峰命名法
    @Insert(value = {"insert into ", TABLE_NAME, "( ", INSERT_FIELDS,
            " ) values (#{content}, #{userId}, #{createdDate}, #{entityId}, #{entityType})"})
    int addComment(Comment comment);

    @Select(value = {" select ", SELECT_FIELDS ," from " , TABLE_NAME, " where id = #{id}"})
    Comment selectCommentById(int id);
}
