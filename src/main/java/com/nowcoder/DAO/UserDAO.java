package com.nowcoder.DAO;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "UserDAO")
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id , " + INSERT_FIELDS;

    //使用MyBatis
    @Insert(value = {"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select(value = {"select ", SELECT_FIELDS, " from " , TABLE_NAME, " where id = #{id}"})
    User selectById(int id);

    @Update(value = {"update ", TABLE_NAME," set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    //不要使用delete
    @Delete(value = {"delete from ", TABLE_NAME, " where id = #{id}"})
    void deleteById(int id);
}
