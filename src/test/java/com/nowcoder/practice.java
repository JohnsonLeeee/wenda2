package com.nowcoder;

import java.util.Random;
import java.util.UUID;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

public class practice {
    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        Random random = new Random();
        System.out.println(random.nextInt(9999));
    }

}
