package com.durant;


import java.util.*;
import java.util.stream.Collectors;

/**
 * 使用DFA算法 匹配多个关键字
 * @author jcy
 */
public class KeyWordUtil {

    /**
     * 创建一个map，里面用来存关键字
     */
    private Map<String, Object> dictionaryMap;

    /**
     * 构造器，传入一个set，然后生成一个dictionaryMap
     * @param wordSet
     */
    public KeyWordUtil(Set<String> wordSet) {
        this.dictionaryMap = handleToMap(wordSet);
    }

    /**
     * 构造器，传入一个set，然后生成一个dictionaryMap,注意：需要自己保证list数据不重复
     * @param wordList
     */
    public KeyWordUtil(List<String> wordList) {
        this.dictionaryMap = handleToMap(wordList);
    }

    /**
     * 获取配置好的数据字典
     * @return Map<String, Object>
     */
    public Map<String, Object> getDictionaryMap() {
        return dictionaryMap;
    }

    /**
     * 设置一个数据字典Map<String, Object>
     * @param dictionaryMap
     */
    public void setDictionaryMap(Map<String, Object> dictionaryMap) {
        this.dictionaryMap = dictionaryMap;
    }


    private Map<String, Object> handleToMap(List<String> wordList) {
        if (wordList != null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(wordList.size());
        Iterator<String> ite = wordList.iterator();
        getWantMap(ite,map);
        return map;
    }


    /**
     * 抽取相同部分成方法
     * @param ite
     * @param map
     */
    private void getWantMap(Iterator<String> ite,Map<String, Object> map){
        Map<String, Object> curMap;
        while (ite.hasNext()) {
            // 取出当前的关键词
            String word = ite.next();
            curMap = map;
            int len = word.length();
            for (int i = 0; i < len; i++) {
                String key = String.valueOf(word.charAt(i));
                Map<String, Object> wordMap = (Map<String, Object>) curMap.get(key);
                if (wordMap == null) {
                    wordMap = new HashMap<>();
                    wordMap.put("isEnd", "0");
                    curMap.put(key, wordMap);
                    curMap = wordMap;
                }
                else {
                    curMap = wordMap;
                }
                if (i == len - 1) {
                    curMap.put("isEnd", "1");
                }
            }
        }
    }
    private Map<String, Object> handleToMap(Set<String> wordSet) {
        /**
         * 保证关键字集合不为空
         */
        if (wordSet == null) {
            return null;
        }
        /**
         * 创建一个map 来存放最终的字典
         */
        Map<String, Object> map = new HashMap<>(wordSet.size());
        Iterator<String> ite = wordSet.iterator();
        getWantMap(ite,map);
        return map;
    }

    @SuppressWarnings("unchecked")
    public int checkWord(String text, int beginIndex) {
        if (dictionaryMap == null) {
            throw new RuntimeException("字典不能为空！");
        }
        boolean isEnd = false;
        int wordLength = 0;
        Map<String, Object> curMap = dictionaryMap;
        int len = text.length();
         for (int i = beginIndex; i < len; i++) {
            String key = String.valueOf(text.charAt(i));
            curMap = (Map<String, Object>) curMap.get(key);
            if (curMap == null) {
                curMap = (Map<String, Object>) dictionaryMap.get(key);
                if (curMap == null){
                    curMap = dictionaryMap;
                    continue;
                }
                if ("1".equals(curMap.get("isEnd"))) {
                    wordLength++;
                    isEnd = true;
                }
            }
            else {
                if ("1".equals(curMap.get("isEnd"))) {
                    wordLength++;
                    isEnd = true;
                    curMap = dictionaryMap;
                }
            }
        }
        if (!isEnd) {
            wordLength = 0;
        }
        return wordLength;
    }

    public Set<String> getWords(String text) {
        Set<String> wordSet = new HashSet<>();
        int len = text.length();
        for (int i = 0; i < len; i++) {
            int wordLength = checkWord(text, i);
            if (wordLength > 0) {
                String word = text.substring(i, i + wordLength);
                wordSet.add(word);
                i = i + wordLength - 1;
            }
        }
        return wordSet;
    }
}
