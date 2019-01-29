package com.nowcoder.service;

import com.nowcoder.model.Question;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: wenda
 * @description: 全文搜索
 * @author: Li Shuai
 * @create: 2019-01-28 13:11
 **/
@Service
public class SearchService {
    @Autowired
    QuestionService questionService;

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    /* solrj的用法
        The center of SolrJ is the org.apache.solr.client.solrj package, which contains just five main classes. Begin by creating a SolrClient, which represents the Solr instance you want to use. Then send SolrRequests or SolrQuerys and get back SolrResponses.
        SolrClient is abstract, so to connect to a remote Solr instance, you’ll actually create an instance of either HttpSolrClient, or CloudSolrClient. Both communicate with Solr via HTTP, the difference is that HttpSolrClient is configured using an explicit Solr URL, while CloudSolrClient is configured using the zkHost String for a SolrCloud cluster.
     */
    private static final String urlString = "http://localhost:8983/solr/wenda";
    private SolrClient solr = new HttpSolrClient.Builder(urlString).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";

    public List<Question> searchQuestion(String keyword, int offset, int count,
                                         String highlightPre, String highlightPost) throws SolrServerException, IOException {
        List<Question> questions = new ArrayList<>(20);
        // Use query() to have Solr search for results. You have to pass a SolrQuery object that describes the query, and you will get back a QueryResponse (from the org.apache.solr.client.solrj.response package).
        SolrQuery query = new SolrQuery();
        // 设置查询关键词
        query.setQuery(keyword);
        // 设置偏移量
        query.setStart(offset);
        query.setRows(count);
        // 设置高亮、高亮前缀和后缀
        query.setHighlight(true);
        query.addHighlightField(QUESTION_CONTENT_FIELD +"," + QUESTION_TITLE_FIELD);
        query.setHighlightSimplePre(highlightPre);
        query.setHighlightSimplePost(highlightPost);
        // query.set("hl.fl", QUESTION_CONTENT_FIELD +"，" + QUESTION_TITLE_FIELD);

        // 设置fields,df
        // query.setFields(QUESTION_CONTENT_FIELD, QUESTION_TITLE_FIELD);
        // 上条代码等同于query.set("fl", QUESTION_CONTENT_FIELD +"，" + QUESTION_TITLE_FIELD);

        // Once you have your SolrQuery set up, submit it with query():
        QueryResponse response = solr.query(query);

        // logger.info(response.toString());
        logger.info(response.getHighlighting().toString());

        // The client makes a network connection and sends the query. Solr processes the query, and the response is sent and parsed into a QueryResponse.

        // The QueryResponse is a collection of documents that satisfy the query parameters. You can retrieve the documents directly with getResults() and you can call other methods to find out information about highlighting or facets.
        //
        //SolrDocumentList list = response.getResults();

        // 解析response
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            int questionId = Integer.parseInt(entry.getKey());
            Question question = questionService.getQuestionById(questionId);

            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> content = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (content.size() > 0) {
                    question.setContent(content.get(0));
                }
            }

            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> title = entry.getValue().get(QUESTION_TITLE_FIELD);
                if (title.size() > 0) {
                    question.setTitle(title.get(0));
                }
            }

            logger.info(question.getTitle());

            questions.add(question);

        }
        return questions;
    }

    public boolean IndexQuestion(int qid, String title, String content) throws SolrServerException, IOException {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", qid);
        document.addField(QUESTION_TITLE_FIELD, title);
        document.addField(QUESTION_CONTENT_FIELD, content);

        UpdateResponse response = solr.add(document);
        return response != null && response.getStatus() == 0;
    }
}
