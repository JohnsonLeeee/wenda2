package com.nowcoder.async;

/**
 * @program: wenda
 * @description: 事件类型
 * @author: Li Shuai
 * @create: 2019-01-04 19:22
 **/

public enum EventType {
    LIKE(1), COMMENT(2), LOGIN(3),
    MAIL(4), FOLLOW(5), UNFOLLOW(6),
    ADD_QUESTION(7), ANSWER_QUESTION(8),
    COMMENT_COMMENT(9), FOLLOW_USER(10),
    UNFOLLOW_USER(11), FOLLOW_QUESTION(12),
    UNFOLLOW_QUESTION(13);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
