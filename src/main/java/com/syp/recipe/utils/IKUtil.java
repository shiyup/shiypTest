package com.syp.recipe.utils;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

/**
 * created by shiyuping on 2019/1/29
 */
public class IKUtil {

    public void test(){
        Analyzer analyzer = new IKAnalyzer();
    }

    public static void main(String[] args) throws IOException {
        String text="双氯芬酸二乙胺乳酸剂";
        //创建分词对象
        Analyzer anal=new IKAnalyzer(true);
        StringReader reader=new StringReader(text);
        //分词
        TokenStream ts=anal.tokenStream("", reader);
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);

        ts.reset();
        //遍历分词数据
        while(ts.incrementToken()){
            System.out.print(term.toString()+"|");
        }
        reader.close();
        System.out.println();
    }
}
