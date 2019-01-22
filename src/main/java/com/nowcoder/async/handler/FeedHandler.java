package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @program: wenda
 * @description: 异步处理feed流
 * @author: Li Shuai
 * @create: 2019-01-22 17:37
 **/
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;

    @Override
    public void doHandle(EventModel model) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getEventType().getValue());
        feed.setData(buildData(model));
        if (feed.getData() == null) {
            return;
        }
        feedService.addFeed(feed);
    }

    private String buildData(EventModel eventModel) {
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUser(eventModel.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        // 如果关注了问题，或者回答了问题，把question的信息添加进feed.data里。
        if (eventModel.getEventType() == EventType.ANSWER_QUESTION ||
                (eventModel.getEventType() == EventType.FOLLOW &&
                        eventModel.getCarrierEntityType()== EntityType.QUESTION)) {
            Question question = questionService.getQuestionById(eventModel.getCarrierEntityId());
            if (question == null) {
                return null;
            }

            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());

            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW, EventType.ADD_QUESTION,
                EventType.LIKE, EventType.ANSWER_QUESTION);
    }
}
