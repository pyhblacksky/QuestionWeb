package com.nowcoder.DAO;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

//Ticket的作用是保存登录状态信息的一个句柄
@Mapper
@Component("LoginTicketDAO")
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id " + INSERT_FIELDS;

    @Insert(value = {"insert into ", TABLE_NAME, " ( ", INSERT_FIELDS,
            ") values(#{userId}, #{expired}, #{status}, #{ticket})"})
    int addTicket(LoginTicket loginTicket);

    @Select(value = {"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket = #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update(value = {" update ", TABLE_NAME, " set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

}
