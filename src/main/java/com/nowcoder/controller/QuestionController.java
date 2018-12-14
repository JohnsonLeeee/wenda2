package com.nowcoder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

public class QuestionController {
    /*
     * private - so that no other class can hijack your logger
     * static - so there is only one logger instance per class, also avoiding attempts to serialize loggers
     * final - no need to change the logger over the lifetime of the class
     */
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

}
