package com.nowcoder.search;

import com.nowcoder.WendaApplication;
import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.Date;

/**
 * @program: wenda
 * @description: 测试SearchService
 * @author: Li Shuai
 * @create: 2019-01-30 12:19
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class SearchServiceTests {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceTests.class);
    @Autowired
    SearchService searchService;
    @Autowired
    QuestionService questionService;

    @Before
    public void before() {
        logger.info("@before执行");
        Question question = new Question();
        question.setTitle("search测试");
        question.setContent("search测试");
        question.setUserId(1);
        question.setCommentCount(0);
        question.setCreatedDate(new Date());
        questionService.addQuestion(question);
        try {
            Assert.assertTrue(searchService.IndexQuestion(question.getId(), question.getTitle(), question.getContent()));
        } catch (SolrServerException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void after() {
        logger.info("@After执行");
    }

    @Test
    public void testSearch() {
        logger.info("@Test执行");
        try {
            for (Question question:
                    searchService.searchQuestion("贔", 0, 20, "<em>", "</em>")) {
                Assert.assertTrue(question.getTitle().contains("贔") || question.getContent().contains("贔"));
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSearch2() {
        logger.info("@Test2执行");
    }

    @BeforeClass
    public static void beforeClass() {
        logger.info("@BeforeClass");
    }

    @AfterClass
    public static void AfterClass() {
        logger.info("@AfterClass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        logger.info("试用测试Exception功能");
        throw new IllegalArgumentException();
    }
}
