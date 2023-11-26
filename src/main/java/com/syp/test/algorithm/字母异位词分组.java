package com.syp.test.algorithm;

import java.util.*;

/**
 * 给你一个字符串数组，请你将 字母异位词 组合在一起。可以按任意顺序返回结果列表。
 *
 * 字母异位词 是由重新排列源单词的所有字母得到的一个新单词。
 *
 *
 *
 * 示例 1:
 *
 * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
 * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
 * 示例 2:
 *
 * 输入: strs = [""]
 * 输出: [[""]]
 * 示例 3:
 *
 * 输入: strs = ["a"]
 * 输出: [["a"]]
 *
 *
 * 提示：
 *
 * 1 <= strs.length <= 104
 * 0 <= strs[i].length <= 100
 * strs[i] 仅包含小写字母
 *
 * @author shiyuping
 */
public class 字母异位词分组 {

    /**
     * 方法一
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {

        List<List<String>> res = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            //排序
            char[] array = str.toCharArray();
            Arrays.sort(array);
            String key = new String(array);
            if (map.containsKey(key)){
                map.get(key).add(str);
            }else {
                List<String> list = new ArrayList<>();
                list.add(str);
                map.put(key, list);
            }
        }
        map.forEach((k,v)-> res.add(v));
        return res;
    }

    /**
     * 方法二
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] array = str.toCharArray();
            Arrays.sort(array);
            String key = new String(array);
            List<String> list = map.getOrDefault(key, new ArrayList<>());
            list.add(str);
            map.put(key, list);
        }
        return new ArrayList<>(map.values());
    }
}
