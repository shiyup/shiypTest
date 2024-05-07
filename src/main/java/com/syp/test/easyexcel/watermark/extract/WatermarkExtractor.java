package com.syp.test.easyexcel.watermark.extract;

import java.io.File;

/**
 * Created by shiyuping on 2024/5/7 10:11 AM
 * 水印提取器
 * @author shiyuping
 */
public class WatermarkExtractor {


    /**
     * 提取图片/excel水印
     * @param file
     * @return
     */
    public static String extract(File file) {
        //获取文件后缀
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        AbstractWatermarkExtractHandler extractHandler = AbstractWatermarkExtractHandler.getExtractHandler(suffix);
        return extractHandler.extract(file);
    }

    /**
     * 提取纯文本水印
     * @param text
     * @return
     */
    public static String extract(String text) {
        return "";
    }
}
