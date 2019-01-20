package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: pyh
 * @Date: 2019/1/20 10:06
 * @Version 1.0
 * @Function:
 *      异步处理框架之
 *          事件模型——共有的部分。当时时间发生的现场
 *          变量独立为一个类
 */
public class EventModel {

    private EventType type;//事件类型
    private int actorId;//触发者，谁触发

    //触发对象
    private int entityType;//实体类型，如问题、评论是不同的实体
    private int entityId;//实体id,依赖于实体类型
    private int entityOwnerId;//触发的对象所有者，即与触发对象的关联者

    //扩展字段，类似vo，存储对象
    private Map<String, String> exts = new HashMap<>();

    //方便扩展字段传递,k - v形式传递
    public EventModel setExt(String key, String value){
        exts.put(key, value);
        return this;//为了链式调用
    }
    public String getExt(String key){
        return exts.get(key);
    }

    /**
     * 在以下set方法中，以EventModel作为返回值。为了调用方便.链式调用方法
     *  链式设置值，如  xx.setType().setXX().set() ....   可以连续设置值
     * */

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
