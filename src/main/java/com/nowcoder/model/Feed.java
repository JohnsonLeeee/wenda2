package com.nowcoder.model;

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
