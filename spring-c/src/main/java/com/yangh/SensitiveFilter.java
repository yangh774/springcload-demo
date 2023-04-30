package com.yangh;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author "杨航_2211"
 * @Descript {
 * 介绍：
 * }
 * @Project SpringCloud-Alibaba
 * @date 2023/04/27 下午 06:47
 */
@Component
public class SensitiveFilter {

    // 这是slf4j包的、Logger记录器的意思
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换的内容
    private static final String REPLACEMENT = "**";

    // 前缀树根节点
    private TrieNode rootNode = new TrieNode() {};

    // 第一步：定义前缀树结构
    private class TrieNode {
        // 敏感词结束标识
        private boolean isKeyWordEnd = false;

        // 子节点 (key=下级字符、value是下级节点) 字符对应子节点 默认是空的 Character是一个类、用于对单个字符进行操作
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        // 提供对应访问方法
        // 添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c, node);
        }
        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

    }

    // 第二步：初始化敏感词、这是个初始化注解、会被执行
    @PostConstruct
    public void init() {
        // 使用类加载器、加载文件 try(){}语法是1.7引入的、用于简化关闭流的系列操作
        try (
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("filtertext.txt");
            // 转为缓冲流
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            // 读一行
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (Exception e) {
           logger.error("加载敏感词文件失败");
        }
    }

    // 提供方法、将字符添加到前缀树中
    private void addKeyword(String keyword) {
        // 临时节点 = 初始化的根节点
        TrieNode tempNode = rootNode;
        // 遍历传递的字符串
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c); // 去前缀树里面找、看看有没有这个节点
            if (subNode == null) {
                // 如果没有就初始化子节点
                 subNode = new TrieNode(); // 挂到当前节点
                tempNode.addSubNode(c,subNode);
            }
            // 指向子节点、进入下一轮循环 就是一直存节点
            tempNode = subNode;

            // 设置结束标识
            if (i == keyword.length()-1) {
                tempNode.setKeyWordEnd(true); //设置为结束
            }
        }
    }

    // 第三步：编译过滤敏感词的方法、返回过滤好的
    public String filter(String text) {
        if (text == null) {return null;}

        // 指针1、初始指向根节点
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;

        // 记录最终结果 减少内存开支多次
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);
            // 跳过符合比如^$赌@#博*^看这 如果是符号 就要跳过
            if (this.isSymbol(c)) {
                // 如果当前指针处于根节点、讲此符号计入结果、让指针2向下走
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                position++; // 这就代表不是根节点也+1
                continue; // 跳出当前循环 执行下一次循环
            }
            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd()) {
                // 发现敏感词、将begin-position区间字符串替换
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                position++;
            }
        }
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 取反 是否是Ascii字母、是否是东亚文字范围内
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0xFFFF);
    }


}
