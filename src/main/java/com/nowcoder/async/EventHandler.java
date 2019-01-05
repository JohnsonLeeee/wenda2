package com.nowcoder.async;

import java.util.List;

/**
 * @program: wenda
 * @description: EventHandler
 * @author: Li Shuai
 * @create: 2019-01-04 20:14
 **/

public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
