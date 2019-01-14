package com.nowcoder.configuration;

import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: pyh
 * @Date: 2019/1/14 21:20
 * @Version 1.0
 * @Function:扩展接口实现增加自己的拦截器！必要
 *              否则自己的拦截器功能无法实现
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    PassportInterceptor passportInterceptor;

    //注册自己的拦截器，将自己写的拦截器实现
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //在系统初始化之前添加拦截器,此时才算真正的添加了自己的拦截器，注册到整个链路
        registry.addInterceptor(passportInterceptor);
        super.addInterceptors(registry);
    }
}
