package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: wenda
 * @description: 用户被关注，发站内信
 * @author: Li Shuai
 * @create: 2019-01-08 16:44
 **/
@Component
public class FollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW, EventType.UNFOLLOW);
    }

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USER_ID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setConversationId(WendaUtil.SYSTEM_USER_ID, model.getEntityOwnerId());
        User user = userService.getUser(model.getActorId());
        if (model.getEntityType() == EntityType.QUESTION) {
            message.setContent("用户" + user.getName() +
                    "关注了你的问题，http://127.0.0.1:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.USER) {
            message.setContent("用户" + user.getName() +
                    "关注了你，http://127.0.0.1:8080/userId/" + model.getActorId());
        }

        messageService.addMessage(message);
    }
}
