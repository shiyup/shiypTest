package com.syp.test.algorithm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 *
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 示例 2：
 *
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出：9
 *
 *
 * 提示：
 *
 * 0 <= nums.length <= 105
 * -109 <= nums[i] <= 109
 */
public class 最长连续序列 {

    public static int longestConsecutive(int[] nums) {

        Set<Integer> intSet = new HashSet<>();
        for (int num : nums) {
            intSet.add(num);
        }
        List<Integer> arrayList = intSet.stream().sorted().collect(Collectors.toList());

        if(arrayList.size() == 1){
            return 1;
        }
        Set<Integer> res = new HashSet<>();
        Set<Integer> compare = new HashSet<>();
        for (int i = 0; i < arrayList.size() -1; i++) {
            int num = arrayList.get(i);
            int nextNum = arrayList.get(i+1);
            res.add(num);
            if (nextNum == num + 1){
                res.add(num + 1);
            }else {
                if (compare.size() <= res.size()){
                    compare = res;
                }
                res = new HashSet<>();
            }
        }
        if (compare.size() <= res.size()){
            compare = res;
        }

        return compare.size();
    }

    public int longestConsecutive2(int[] nums) {
        //去重
        Set<Integer> num_set = new HashSet<Integer>();
        for (int num : nums) {
            num_set.add(num);
        }

        //最长序列数
        int longestStreak = 0;

        for (int num : num_set) {
            if (!num_set.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;

                while (num_set.contains(currentNum + 1)) {
                    currentNum += 1;
                    currentStreak += 1;
                }

                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }

        return longestStreak;
    }


    public static void main(String[] args) {
        int[] nums = {4,0,-4,-2,2,5,2,0,-8,-8,-8,-8,-1,7,4,5,5,-4,6,6,-3};
        System.out.println(longestConsecutive(nums));
    }
}
