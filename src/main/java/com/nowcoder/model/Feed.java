package com.nowcoder.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @program: wenda
 * @description: 新鲜事流
 * @author: Li Shuai
 * @create: 2019-01-10 16:24
 **/

public class Feed {
    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    // json
    private String data;

    private JSONObject dataJSON;

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
        dataJSON = JSONObject.parseObject(data);
    }


    // issue : 这里用get是为了在velocity中使用.进行引用，因为velocity 自动引用带
    // get、is的方法
    //     // tip: 为了适配velocity
    //    // obj.xxx (velocity中相当于使用以下方法)
    //    // obj.get("xxx");
    //    // obj.getXXX();
    //    // obj.isXXX();
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
