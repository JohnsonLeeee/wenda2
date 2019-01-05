package com.nowcoder.async;

/**
 * @program: wenda
 * @description: 事件类型
 * @author: Li Shuai
 * @create: 2019-01-04 19:22
 **/

public enum EventType {
    LIKE(1), COMMENT(2), LOGIN(3), MAIL(4);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
