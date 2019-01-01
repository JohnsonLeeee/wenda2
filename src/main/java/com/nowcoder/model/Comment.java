package com.nowcoder.model;

import java.util.Date;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

public class Comment {
    private int id;
    private String content;
    private int userId;
    private int entityType;
    private int entityId;
    private Date createdDate;
    private int status;

    public void setEntityType(EntityType entityType) {
        switch (entityType) {
            case QUESTION:
                this.entityType = 1;
                break;
            case COMMENT:
                this.entityType = 2;
                break;

        }
    }

    public EntityType getEntityTypeEnum() {
        if (entityType == 1) {
            return EntityType.QUESTION;
        } else if (entityType == 2) {
            return EntityType.COMMENT;
        }
        return EntityType.QUESTION;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
