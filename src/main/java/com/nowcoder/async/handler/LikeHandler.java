package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: wenda
 * @description: 用户点赞，给其发站内信
 * @author: Li Shuai
 * @create: 2019-01-04 22:27
 **/
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USER_ID);
        message.setToId(model.getCarrierEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setConversationId(WendaUtil.SYSTEM_USER_ID, model.getCarrierEntityOwnerId());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() +
                "赞了你的评论，http://127.0.0.1:8080/question/" + model.getExts("questionId"));

        messageService.addMessage(message);

    }
}
