package com.nowcoder.service;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    MySensitiveService mySensitiveService;

    public Question getQuestionById(int questionId) {
        return questionDAO.selectQuestionById(questionId);
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public int addQuestion(Question question) {

        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setTitle(mySensitiveService.filter(question.getTitle()));
        question.setContent(mySensitiveService.filter(question.getContent()));
        // TODO 敏感词过滤
        // lishuai_todo 练习todo的使用
        // fixme fixme的使用


        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public boolean updateCommentCount(int questionId, int count) {
        return questionDAO.updateCommentCount(questionId, count);
    }
}
