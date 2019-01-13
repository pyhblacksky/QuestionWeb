package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

//传递对象和velocity的一个中间对象，不是视图
public class ViewObject {

    private Map<String, Object> objs = new HashMap<>();

    public void set(String key, Object value){
        objs.put(key, value);
    }

    public Object get(String key){
        return objs.get(key);
    }

}
