package com.syp.test.algorithm;

import com.google.common.collect.Lists;

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

        System.out.println( permutationNoRepeat(list, 3));

        zuhe(list, Lists.newArrayList(), 3,0,0);
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
}
