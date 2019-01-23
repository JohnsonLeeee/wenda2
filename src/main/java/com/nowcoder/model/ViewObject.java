package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    // tip: 为了适配velocity
    // obj.xxx (velocity中相当于使用以下方法)
    // obj.get("xxx");
    // obj.getXXX();
    // obj.isXXX();
    public Object get(String key) {
        return objs.get(key);
    }
}
