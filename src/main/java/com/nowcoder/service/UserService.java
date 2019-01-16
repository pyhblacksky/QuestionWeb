package com.nowcoder.service;

import com.nowcoder.DAO.LoginTicketDAO;
import com.nowcoder.DAO.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    //注册服务
    public Map<String, Object> register(String username, String password){
        Map<String, Object> map = new HashMap<>();
        //判断合法性
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg","用户名已经被注册");
            return map;
        }

        //注册
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    //登录服务
    public Map<String, Object> login(String username, String password){
        Map<String, Object> map = new HashMap<>();
        //判断合法性
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg","用户名不存在");
            return map;
        }

        //登录验证
        if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);  //传出去，通过http下发
        return map;
    }

    //下发ticket
    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600*24*10000 + now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    //登出功能，将status设置为1，则无效
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
