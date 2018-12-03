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

    public void setObjs(String key, Object value) {
        objs.put(key, value);
    }

    public Object getObject(String key) {
        objs.get(key);
    }
}
