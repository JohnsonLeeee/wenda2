package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.service.SearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @program: wenda
 * @description: 增加问题后，建立索引
 * @author: Li Shuai
 * @create: 2019-01-30 00:20
 **/
@Component
public class QuestionHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(QuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        // logger.info("执行了questionhandler");
        int questionId = model.getCarrierEntityId();
        String title = model.getExts("title");
        String content = model.getExts("content");
        try {
            searchService.IndexQuestion(questionId, title, content);
        } catch (SolrServerException | IOException e) {
            logger.error("添加题目索引失败");
            e.printStackTrace();
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION, EventType.ANSWER_QUESTION);
    }
}
