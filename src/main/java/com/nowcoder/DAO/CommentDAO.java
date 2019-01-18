package com.nowcoder.DAO;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "CommentDAO")
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id , " + INSERT_FIELDS;
    //注意区分什么时候是下划线_命名法，什么时候是驼峰命名法
    @Insert(value = {"insert into ", TABLE_NAME, "( ", INSERT_FIELDS,
            " ) values (#{content}, #{userId}, #{createdDate}, #{entityId}, #{entityType}, #{status})"})
    int addComment(Comment comment);

    @Select(value = {" select ", SELECT_FIELDS ," from " , TABLE_NAME,
            " where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select(value = {"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update(value = {"update comment set status=#{status} where id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

    @Select(value = {" select ", SELECT_FIELDS ," from " , TABLE_NAME, " where id = #{id}"})
    Comment selectCommentById(int id);
}
