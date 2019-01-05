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
    private EventType eventType;
    private int actorId;
    private EntityType entityType;
    private int entityId;
    private int entityOwnerId;
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

    public EntityType getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(EntityType entityType) {
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
