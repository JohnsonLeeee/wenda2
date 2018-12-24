package com.nowcoder.service;

import jdk.internal.util.xml.impl.Input;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import sun.text.normalizer.Trie;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: wenda
 * @description: 自己实现的SensitiveService类和敏感词过滤算法,第五课
 * @author: Li Shuai
 * @create: 2018-12-23 19:05
 **/

@Service
public class MySensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MySensitiveService.class);

    // 初始化根节点
    private TrieNode rootNode = new TrieNode();

    // todo 外部文件读取所有敏感词
    // issue: Initializing的用法咋用
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            // 注意这里读取文件的方法，ResourceUtils.getFile("classpath:SensitiveWords.txt");
            File sensitiveWordsFile = ResourceUtils.getFile("classpath:SensitiveWords.txt");
            FileReader fileReader = new FileReader(sensitiveWordsFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String sensitiveWord;
            // issue: 为什么while里可以写赋值语句
            while ((sensitiveWord = bufferedReader.readLine()) != null) {
                addSensitiveWord(sensitiveWord.trim());
            }
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            logger.error("读取敏感词文件错误");
        }

    }

    // 构造前缀树
    class TrieNode {
        private boolean isEnd = false;

        // 这里用HashMap而不用List的原因：查找子节点时，List需要遍历，O(n)复杂度；
        // HashMap不用，直接根据Character的值计算出下一个节点的地址，是O(1)复杂度
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        // 添加子节点
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isEnd() {
            return isEnd;
        }

        void setEnd(boolean isWordEnd) {
            this.isEnd = isWordEnd;
        }

    }

    // 添加敏感词
    public void addSensitiveWord(String sensitiveWord) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < sensitiveWord.length(); i++) {
            char c = sensitiveWord.charAt(i);
            // 忽略@#等符号
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = tempNode.getSubNode(c);
        }
        tempNode.setEnd(true);
    }

    // 敏感词过滤算法
    public String filter(String text) {
        /* 敏感词算法：双指针算法指针；
        int position 指针用来指示目前的位置；
        int charIndex 指针用来检测text敏感词；
        StringBuilder result 用来存储被过滤过的字符串；
        */

        StringBuilder result = new StringBuilder();
        String replacement = "萌萌哒";

        text = text.trim();


        // position 指针向后移位
        for(int position = 0; position < text.length(); position++) {
            TrieNode tempNode = rootNode;
            // charIndex 指针向后移位
            boolean finishedForLoop = true;
            for(int charIndex = position; charIndex < text.length(); charIndex++) {
                char character = text.charAt(charIndex);

                // 过滤symbol
                if (isSymbol(character)) {
                    break;
                }

                if (tempNode.getSubNode(character) != null && !tempNode.getSubNode(character).isEnd()) {
                    // 1. character对应的节点存在， 即character在字典树中,但不是终点
                    // 1. 树的指针向后移动
                    tempNode = tempNode.getSubNode(character);
                } else if (tempNode.getSubNode(character) != null && tempNode.getSubNode(character).isEnd()) {
                    // 2. character对应的节点存在， 即character在字典树中,并且是终点
                    result.append(replacement);
                    position = charIndex;
                    finishedForLoop = false;
                    break;
                } else {
                    // 3. character对应的节点不存在， 即character不在字典树中，跳出charIndex的循环
                    result.append(text.charAt(position));
                    finishedForLoop = false;
                    break;
                }
            }
            // 3. character对应的节点不存在， 即character不在字典树中，跳出charIndex的循环
            // 修复bitc结尾，b找不到的bug
            if (finishedForLoop) {
                result.append(text.charAt(position));
            }

        }
        return result.toString();
    }

    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 东亚文字 0x2E80-0x9FFF,返回非字母和数字，返回非东亚文字，其他都是Symbol
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }





    // 测试敏感词过滤功能
    public static void main(String[] args) {
        MySensitiveService test = new MySensitiveService();
        test.addSensitiveWord("bitch");
        test.addSensitiveWord("沙雕");
        String filter_text = test.filter("bitc");
        System.out.println(filter_text);
        System.out.println(test.filter("你是@沙@雕@吗？"));
        System.out.println(test.filter("你是沙@雕吗？"));
        System.out.println(test.filter("你是沙雕吗？"));
        // y=x表达式返回的是什么
        String x = "dog";
        String y;
        System.out.println(y=x);
        System.out.println(y);
    }
}
