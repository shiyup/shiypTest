package com.syp.test.algorithm;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by shiyuping on 2022/11/16 11:13 下午
 *
 * @author shiyuping
 */
public class 排列组合 {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1","2","3","4","5");

        //System.out.println( permutationNoRepeat(list, 3));

        zuhe(list, Lists.newArrayList(), 4,0,0);
        System.out.println(permutation(list, 3));
        System.out.println(subsets(list.toArray(new String[0])));
    }


    public static List<String> permutationNoRepeat(List<String> list, int length) {
        Stream<String> stream = list.stream().distinct();
        stream = stream.flatMap(str -> list.stream().filter(temp -> !str.contains(temp)).map(str::concat));
        /*for (int n = 1; n < length; n++) {
            stream = stream.flatMap(str -> list.stream().filter(temp -> !str.contains(temp))
                    .map(str::concat));
        }*/
        return stream.collect(Collectors.toList());
    }

    public static void zuhe(List<String> shu, List<String> list, int tag, int has, int cur) {
        if (has == tag) {
            System.out.println(list);
            return;
        }
        for (int i = cur; i < shu.size(); i++) {
            if (!list.contains(shu.get(i))) {
                list.add(shu.get(i));
                zuhe(shu, list, tag, has + 1, i);
                list.remove(list.size() - 1);
            }
        }
    }

    public static List<List<String>> permutation(List<String> value, int num) {
        List<List<String>> valueList = new ArrayList<>();
        return doPermutation(value, Lists.newArrayList(), num,0,0, valueList);
    }
    public static List<List<String>> doPermutation(List<String> shu, List<String> list, int tag, int has, int cur, List<List<String>> resList) {
        if (has == tag) {
            resList.add(list);
            return resList;
        }
        for (int i = cur; i < shu.size(); i++) {
            if (!list.contains(shu.get(i))) {
                list.add(shu.get(i));
                doPermutation(shu, list, tag, has + 1, i, resList);
                list.remove(list.size() - 1);
            }
        }
        return resList;
    }

    public static List<List<String>> subsets(String[] nums) {
        List<List<String>> valueList = new ArrayList<>();
        boolean jud[] = new boolean[nums.length];
        dfs(nums, -1, valueList, jud);
        return valueList;
    }

    private static void dfs(String[] num, int index, List<List<String>> valueList, boolean jud[]) {
        {
            //每进行递归函数都要加入到结果中
            List<String> list = new ArrayList<>();
            for (int i = 0; i < num.length; i++) {
                if (jud[i]) {
                    list.add(num[i]);
                }
            }
            if (CollectionUtil.isNotEmpty(list) && list.size() != 1) {
                valueList.add(list);
            }
        }
        {
            for (int i = index + 1; i < num.length; i++) {
                jud[i] = true;
                dfs(num, i, valueList, jud);
                jud[i] = false;
            }
        }
    }
}
