package com.nowcoder.async;

/**
 * @Author: pyh
 * @Date: 2019/1/20 10:02
 * @Version 1.0
 * @Function:
 *      异步处理框架之
 *          事件类型_枚举型
 */
public enum  EventType {

    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5);


    private int value;
    EventType(int value){this.value = value;}
    public int getValue(){return value;}

}
