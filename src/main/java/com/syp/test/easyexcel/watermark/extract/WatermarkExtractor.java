package com.syp.test.easyexcel.watermark.extract;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.syp.test.easyexcel.util.TextBlindWatermarkUtil;

import java.io.File;

/**
 * Created by shiyuping on 2024/5/7 10:11 AM
 * 水印提取器
 * @author shiyuping
 */
public class WatermarkExtractor {


    /**
     * 提取图片/excel/文本水印
     * @param obj
     * @return
     */
    public static String extract(Object obj) {
        if (obj instanceof File){
            File file = (File) obj;
            //获取文件后缀
            String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            AbstractWatermarkExtractHandler extractHandler = AbstractWatermarkExtractHandler.getExtractHandler(suffix);
            if (ObjectUtil.isEmpty(extractHandler)) {
                return StrUtil.EMPTY;
            }
            return extractHandler.extract(file);
        }

        //提取纯文本水印
        if (obj instanceof String){
            return TextBlindWatermarkUtil.extract((String)obj);
        }
        return StrUtil.EMPTY;
    }
}
