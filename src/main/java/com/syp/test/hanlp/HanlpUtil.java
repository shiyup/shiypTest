package com.syp.test.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shiyuping on 2024/5/10 20:43
 * Hanlp分词
 * @author shiyuping
 */
public class HanlpUtil {

    public static void main(String[] args) {

        Map<String, String> out = getOut("我爱吃薯片，它是红薯油炸而来的，紫罗兰熊猫");

        System.out.println("out = " + out);

    }

    /***
     * hanlp分词
     * @param input
     */

    public static Map<String, String> getOut(String input){

        Map<String, String> resMap = new HashMap<>();
        // 使用Hanlp分词
        Segment segment = HanLP.newSegment();
        // 使用Hanlp分词;允许用户自定义词性字典
        // Segment segment = HanLP.newSegment().enableCustomDictionary(true);
        List<Term> termList = segment.seg(input);
        //遍历分词结果
        for (Term term : termList) {
            //词
            String word = term.toString().substring(0, term.length());
            //词性
            String nature = term.toString().substring(term.length() + 1);
            if (StringUtils.isNotBlank(word) && StringUtils.isNotBlank(nature)) {
                //将词及词性放到Map结果集中
                resMap.put(word, nature);
            }
        }

        return resMap;

    }
}
