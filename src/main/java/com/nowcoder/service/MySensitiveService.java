package com.nowcoder.service;

import sun.text.normalizer.Trie;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: wenda
 * @description: 自己实现的SensitiveService类和敏感词过滤算法
 * @author: Li Shuai
 * @create: 2018-12-23 19:05
 **/

public class MySensitiveService {

    // 初始化根节点
    TrieNode rootNode = new TrieNode();

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

        void setEnd(boolean isKey) {
            this.isEnd = isKey;
        }

    }

    // 添加敏感词
    public void addSensitiveWord(String sensitiveWord) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < sensitiveWord.length(); i++) {
            char c = sensitiveWord.charAt(i);
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

        // position 指针向后移位
        for(int position = 0; position < text.length(); position++) {
            TrieNode tempNode = rootNode;
            // charIndex 指针向后移位
            for(int charIndex = position; charIndex < text.length(); charIndex++) {
                char character = text.charAt(charIndex);

                if (tempNode.getSubNode(character) != null && !tempNode.getSubNode(character).isEnd()) {
                    // 1. character对应的节点存在， 即character在字典树中,但不是终点
                    // 1. 树的指针向后移动
                    tempNode = tempNode.getSubNode(character);
                } else if (tempNode.getSubNode(character) != null && tempNode.getSubNode(character).isEnd()) {
                    // 2. character对应的节点存在， 即character在字典树中,并且是终点
                    result.append(replacement);
                    position = charIndex;
                    break;
                } else {
                    // 3. character对应的节点不存在， 即character不在字典树中，跳出charIndex的循环
                    result.append(text.charAt(position));
                    break;
                }
            }
        }
        return result.toString();
    }



    // todo 外部文件读取所有敏感词

    // 测试敏感词过滤功能
    public static void main(String[] args) {
        MySensitiveService test = new MySensitiveService();
        test.addSensitiveWord("bitch");
        String filter_text = test.filter("A bit is not a bitch.");
        System.out.println(filter_text);
    }
}
