package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * @Author: pyh
 * @Date: 2019/1/14 21:10
 * @Version 1.0
 * @Function:用这个类专门放取出的对象,AOP思想
 */
@Component
public class HostHolder {

    //需要保证线程安全,每个线程中都拥有，通过公共接口访问
    private static ThreadLocal<User> users = new ThreadLocal<>();

    //获得当前线程变量所保存的值的方法
    public User getUser(){
        return users.get();
    }

    public void setUsers(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
