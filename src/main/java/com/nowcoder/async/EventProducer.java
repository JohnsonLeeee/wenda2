package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: wenda
 * @description: EventProducer
 * @author: Li Shuai
 * @create: 2019-01-04 19:59
 **/
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {

        // BlockingQueue<EventModel> queue = new ArrayBlockingQueue<EventModel>(16);
        String json = JSONObject.toJSONString(eventModel);
        String key = RedisKeyUtil.getEventQueueKey();


        if (jedisAdapter.lPush(key, json) > 0 ) {
            return true;
        } else {
            return false;
        }



    }
}
