package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: wenda
 * @description: EventConsumer,分发handler
 * @author: Li Shuai
 * @create: 2019-01-04 20:15
 **/
@Service
// ApplicationContextAware: Interface to be implemented by any object that wishes to be notified of the ApplicationContext that it runs in.
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 寻找到所有实现了EventHandler接口的类
        // applicationContext.getBeansOfType：
        // @return a Map with the matching beans, containing the bean names as keys and the corresponding bean instances as values
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            // 遍历beans
            for(Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                // 找到这个event关联的所有eventType
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(() -> {
            while (true) {
                String key = RedisKeyUtil.getEventQueueKey();
                // Redis的阻塞队列
                List<String> events = jedisAdapter.brpop(0, key);
                for (String message : events) {
                    // brpop 会把EVENT_QUEUE这个key先pop出来，故把EVENT_QUEUE过滤掉
                    if (message.equals(key)) {
                        continue;
                    }

                    // 把存储在Redis的list里的序列化的EventModel解析出来。
                    EventModel eventModel = JSON.parseObject(message, EventModel.class);
                    if (!config.containsKey(eventModel.getEventType())) {
                        logger.error("不能识别的事件类型");
                        continue;
                    }

                    // 顺序执行对应EventType的event
                    for (EventHandler handler : config.get(eventModel.getEventType())) {
                        handler.doHandle(eventModel);
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
