package com.nowcoder.interceptor;

import com.nowcoder.DAO.LoginTicketDAO;
import com.nowcoder.DAO.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: pyh
 * @Date: 2019/1/14 21:01
 * @Version 1.0
 * @Function:拦截器，执行一定的拦截功能
 *          构造随处可用的hostHolder
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    //返回false，代表请求失败，整个请求结束
    //在请求开始之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        //先将ticket从cookie中取出
        if(httpServletRequest.getCookies() != null){
            for(Cookie cookie : httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if(ticket != null){
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //满足这些条件，表示是无效的
            if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return true;
            }

            //如果ticket真实有效
            User user = userDAO.selectById(loginTicket.getUserId());
            //依赖注入的思想使其全部有效
            hostHolder.setUsers(user);
        }

        return true;
    }

    //Handle处理完后回调,渲染之前
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //model和view
        if(modelAndView != null){
            //直接传入模板当中，template前端
            //在渲染中，所有页面都可以访问这个user。放入渲染上下文
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    //在渲染后，清除数据
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //结束时，清空
        hostHolder.clear();
    }
}
