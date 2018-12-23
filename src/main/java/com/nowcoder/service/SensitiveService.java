package com.nowcoder.service;

import jdk.internal.util.xml.impl.Input;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: wenda
 * @description: 敏感词过滤
 * @author: Li Shuai
 * @create: 2018-12-23 15:24
 **/

@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    // InitializingBean的用法
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
        } catch (Exception e) {
            logger.error("读取敏感词文件失败");
        }
    }


    // 添加敏感词汇
    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            TrieNode node =  tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;

        }
        tempNode.setEnd(true);
    }

    // 前缀树的构造
    private class TrieNode {
        private boolean end = true;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        public boolean isEnd() {
            return end;
        }

        void setEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();


    // 敏感词过滤
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        String replacement = "萌萌哒";
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while (position < text.length()) {
            char c = text.charAt(position);
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isEnd()) {
                // 发现敏感词
                result.append(replacement);
                position++;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }

        }
        result.append(text.substring(begin));
        return result.toString();
    }

    public static void main(String[] args) {
        SensitiveService s = new SensitiveService();
        s.addWord("沙雕");
        s.addWord("脑残");
        System.out.println(s.filter("你好，沙雕"));
    }
}
