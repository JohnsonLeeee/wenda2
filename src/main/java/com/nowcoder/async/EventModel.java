package com.nowcoder.async;

import com.nowcoder.model.EntityType;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: wenda
 * @description: EventModel 事件的模型
 * @author: Li Shuai
 * @create: 2019-01-04 19:25
 **/

public class EventModel {
    // 事件类型
    private EventType eventType;
    // 触发者的id
    private int actorId;
    // 被触发载体的type和id
    private EntityType carrierEntityType;
    private int carrierEntityId;
    // 拥有这个entity的id，比如点赞后异步发送邮件给被点赞的评论的作者
    private int carrierEntityOwnerId;
    // 其他需要添加的信息
    private Map<String, String> exts = new HashMap<>();

    public EventModel() {}

    public EventModel(EventType type) {
        this.eventType = type;
    }

    public EventModel setExts(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getExts(String key) {
        return exts.get(key);
    }

    public EventType getEventType() {
        return eventType;
    }


    // 返回this的目的：链式调用
    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public EntityType getCarrierEntityType() {
        return carrierEntityType;
    }

    public EventModel setCarrierEntityType(EntityType carrierEntityType) {
        this.carrierEntityType = carrierEntityType;
        return this;
    }

    public int getCarrierEntityId() {
        return carrierEntityId;
    }

    public EventModel setCarrierEntityId(int carrierEntityId) {
        this.carrierEntityId = carrierEntityId;
        return this;
    }

    public int getCarrierEntityOwnerId() {
        return carrierEntityOwnerId;
    }

    public EventModel setCarrierEntityOwnerId(int carrierEntityOwnerId) {
        this.carrierEntityOwnerId = carrierEntityOwnerId;
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
