package com.nowcoder.controller;

import com.nowcoder.async.EventProducer;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.*;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: wenda
 * @description: SearchController
 * @author: Li Shuai
 * @create: 2019-01-28 22:03
 **/
@Controller
public class SearchController {

    private static final String HighlightSimplePre = "<font color=\"red\">";
    private static final String HighlightSimplePost = "</font>";
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @Autowired
    SearchService searchService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search(Model model, @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "20") int count) {
        try {
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count, HighlightSimplePre, HighlightSimplePost);
            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                ViewObject vo = new ViewObject();
                vo.set("question", question);
                vo.set("user", userService.getUser(question.getUserId()));
                vo.set("followCount", followService.getFollowerCount(EntityType.QUESTION, question.getId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);
        } catch (SolrServerException e) {
            e.printStackTrace();
            logger.error("搜索失败，searchService.searchQuestion出错");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("搜索失败，IOException");
        }

        return "result";
    }
}
