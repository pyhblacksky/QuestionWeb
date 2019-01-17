package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
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
 * @Author: pyh
 * @Date: 2019/1/16 17:14
 * @Version 1.0
 * @Function:敏感词过滤服务，使用前缀树
 */
@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    //前缀树根
    private TrieNode rootNode = new TrieNode();

    //初始化Bean，读取敏感词
    @Override
    public void afterPropertiesSet() throws Exception {
        //读取预定义的敏感词文本，构造前缀树
        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            //每次读取一行
            while((lineTxt = bufferedReader.readLine()) != null){
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e){
            logger.error("读取敏感词文本失败" + e.getMessage());
        }
    }

    //构造前缀树，增加关键词的
    private void addWord(String lineTxt){
        //一开始指向根节点
        TrieNode tempNode = rootNode;
        for(int i = 0; i < lineTxt.length(); ++i){
            //找出当前节点
            Character c = lineTxt.charAt(i);

            //如果c无意义，直接跳过
            if(isSymbol(c)){
                continue;
            }

            TrieNode node = tempNode.getSubNode(c);
            //如果没有这个子节点
            if(node == null){
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;
            //如果是最后的节点,标记此节点为最后节点
            if(i == lineTxt.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    //前缀树节点
    private class TrieNode{
        private boolean end = false;    //表示是否是某个敏感词结尾,默认false

        //表示当前节点下的所有子节点，因为可能有多个节点，使用Map表示
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        //在某个字符下挂载节点
        public void addSubNode(Character key, TrieNode node){
            subNodes.put(key, node);
        }

        //根据当前节点获取挂载节点
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        //返回是否是结尾
        boolean isKeyWordEnd(){
            return end;
        }

        //设置是否是尾节点
        void setKeywordEnd(boolean end){
            this.end = end;

        }
    }

    /**
     * 过滤功能代码
     * */
    public String filter(String text){
        //为空，直接返回
        if(StringUtils.isBlank(text)){
            return text;
        }
        //结果字符串
        StringBuilder result = new StringBuilder();

        //替代的字符串,打码
        String replacement = "***";
        TrieNode tempNode = rootNode;   //指向根节点的指针
        int begin = 0;      //表示在字符串中与前缀树对应的当前位置
        int position = 0;   //表示当前判断字符串开始的位置

        while(position < text.length()){
            //取出当前节点值
            char c = text.charAt(position);

            //如果c是非法词，直接跳过
            if(isSymbol(c)){
                //如果一开始为根节点，防止结果丢失
                if(tempNode == rootNode){
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            //取出下一节点
            tempNode = tempNode.getSubNode(c);

            //如果没有，加入sb串，为输出串
            if(tempNode == null){
                //加入以begin为开头的
                result.append(text.charAt(begin));
                //所有位置后移一位
                position = begin + 1;
                begin = position;
                tempNode = rootNode;//重回前缀树根节点
            } else if(tempNode.isKeyWordEnd()){
                //发现敏感词
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                //进入前缀树
                ++position;
            }
        }
        //加上最后未处理的一串字符
        result.append(text.substring(begin));

        return result.toString();
    }

    //增强功能，过滤效果增强，如增加了非中文的东西，空格之类的。判断是否合法
    private boolean isSymbol(char c){
        int ic = (int)c;
        //0x2E80 — 0x9FFF 范围，表示东亚文字
        //isAsciiAlphanumeric表示是英文的词
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    //测试方法
    public static void main(String[] args){
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.println(s.filter("你好赌博欢迎━(*｀∀´*)ノ亻!来赌 不把博    赌 博   赌 博 赌博"));
    }
}
