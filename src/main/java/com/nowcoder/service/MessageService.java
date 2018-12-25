package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: wenda
 * @description: MessageService
 * @author: Li Shuai
 * @create: 2018-12-25 11:21
 **/

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    MySensitiveService mySensitiveService;

    /**
     *
     * @param message message
     * @return 如果插入message成功，返回message的Id，否则返回0
     */
    public int addMessage(Message message) {
        message.setContent(mySensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? message.getId() : 0;
    }
}
