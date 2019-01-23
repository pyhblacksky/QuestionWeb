package com.nowcoder.model;

import java.util.Date;

/**
 * @Author: pyh
 * @Date: 2019/1/23 10:39
 * @Version 1.0
 * @Function:
 *      新鲜事的模型
 *      feed是将用户主动订阅的若干消息源组合在一起形成内容聚合器，
 *      帮助用户持续地获取最新的订阅源内容。
 *      feed流即持续更新并呈现给用户内容的信息流。
 */
public class Feed {

    private int id;
    private int type;//类型
    private int userId;//由谁产生
    private Date createdDate;//创建时间

    //JSON格式，存储各种字段
    private String data;//数据

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
