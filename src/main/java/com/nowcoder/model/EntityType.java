package com.nowcoder.model;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

public enum EntityType {
    QUESTION(1), COMMENT(2);

    private int abbr;

    EntityType(int abbr) {
        this.abbr = abbr;
    }

    public int getAbbr() {
        return abbr;
    }

    public int getIntegerMode() {
        return abbr;
    }
}
