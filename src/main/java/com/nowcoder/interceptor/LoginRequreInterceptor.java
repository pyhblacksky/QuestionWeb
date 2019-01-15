package com.nowcoder.interceptor;

import com.nowcoder.model.HostHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * @Author: pyh
 * @Date: 2019/1/15 11:22
 * @Version 1.0
 * @Function:需要登录权限的拦截器
 */
@Component
public class LoginRequreInterceptor implements HandlerInterceptor {

    @Autowired
    HostHolder hostHolder;

    //判断需要登录
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //如果没有登录，跳转到登录页面
        if(hostHolder.getUser() == null){

            //并传入当前页面作为参数，登录成功后可以跳转
            httpServletResponse.sendRedirect("/reglogin?next="+httpServletRequest.getRequestURI());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
