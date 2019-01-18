package com.nowcoder;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: pyh
 * @Date: 2019/1/18 9:29
 * @Version 1.0
 * @Function: 自写SensitiveService 功能测试，即敏感词过滤
 */
public class TestSensitiveService {

    //前缀树节点
    private class TrieNode{
        private boolean end = false;
        private Map<Character, TrieNode> map = new HashMap<>();

        public void addSubNode(Character c, TrieNode node){
            map.put(c, node);
        }

        public TrieNode getSubNode(Character c){
            return map.get(c);
        }

        public boolean isEnd() {
            return end;
        }

        public void setEnd(boolean end){
            this.end = end;
        }
    }

    private TrieNode root = new TrieNode();

    //构造前缀树
    public void addWord(String txt){
        TrieNode temp = root;

        for(int i = 0; i < txt.length(); i++){
            Character c = txt.charAt(i);

            TrieNode node = temp.getSubNode(c);
            if(node == null){
                node = new TrieNode();
                temp.addSubNode(c, node);
            }
            temp = node;

            if(i == txt.length()-1){
                temp.setEnd(true);
            }
        }
    }

    //过滤代码
    public String filter(String txt){
        if(StringUtils.isBlank(txt)){
            return txt;
        }

        TrieNode temp = root;
        int postion = 0;
        int begin = 0;

        StringBuilder result = new StringBuilder();
        String replacement = "***";

        while(postion < txt.length()){
            char c = txt.charAt(postion);

            temp = temp.getSubNode(c);
            if(temp == null){
                result.append(txt.charAt(begin));
                postion = begin+1;
                begin = postion;
                temp = root;
            } else if(temp.isEnd()){
                result.append(replacement);
                postion = postion + 1;
                begin = postion;
                temp = root;
            } else{
                postion++;
            }
        }
        result.append(txt.substring(begin));

        return result.toString();
    }

    public static void main(String[] args){
        TestSensitiveService t = new TestSensitiveService();
        t.addWord("哈哈");
        t.addWord("你好");
        System.out.println(t.filter("今ddddd天你好，笑一笑哈哈"));
    }

}
