package com.syp.test.algorithm;

import java.util.HashMap;

/**
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 *
 * 示例 1:输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 *
 *
 * 示例 2:输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 *
 *
 * 示例 3:输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3
 * @Author shiyuping
 * @Date 2023/4/23 11:05
 */
public class LengthOfLongestSubstring {

    public static void main(String args[]) {
        String ss="abcabcbb";
        int aa= lengthOfLongestSubstring(ss);
        System.out.print(aa);
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s.length()==0){
            return 0;
        }
        HashMap<Character, Integer> map = new HashMap<>();
        //最大长度
        int max = 0;
        //左指针
        int start = 0;
        for(int end = 0; end < s.length(); end ++){
            //判断有无重复
            if(map.containsKey(s.charAt(end))){
                //containsKey() 方法检查 hashMap 中是否存在指定的 key 对应的映射关系。
                //string.charAt方法返回指定索引处的char值。
                // 取到上次重复的位置 +1
                start = Math.max(start, map.get(s.charAt(end)) + 1);

            }
            map.put(s.charAt(end), end);
            max = Math.max(max, end-start+1);
        }
        return max;

    }

}
