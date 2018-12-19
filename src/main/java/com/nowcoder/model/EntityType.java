package com.nowcoder.model;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

public enum EntityType {
    QUESTION("QUES"), COMMENT("CMT");

    private String abbr;

    EntityType(String abbr) {
        this.abbr = abbr;
    }

    public String getAbbr() {
        return abbr;
    }
}
