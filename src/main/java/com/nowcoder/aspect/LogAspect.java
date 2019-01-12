package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //第一个*：返回值,第二个*：是类名，第三个*：是方法   (..)：是方法的参数
    @Before("execution(* com.nowcoder.controller.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        //这个JoinPoint就是参数变量
        StringBuilder sb = new StringBuilder();
        for(Object arg : joinPoint.getArgs()){
            sb.append("arg: " + arg.toString() + "|");
        }

        logger.info("before method.  "+ sb);
    }

    @After("execution(* com.nowcoder.controller.*.*(..))")
    public void afterMethod(){
        logger.info("after method" + new Date());
    }
}
