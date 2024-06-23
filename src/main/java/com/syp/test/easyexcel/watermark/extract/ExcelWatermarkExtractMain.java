package com.syp.test.easyexcel.watermark.extract;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.syp.test.easyexcel.model.ExcelDataEntity;
import com.syp.test.easyexcel.util.TextBlindWatermarkUtil;

import java.io.File;
import java.util.Map;

/**
 * Created by shiyuping on 2024/4/25 5:56 PM
 * excel水印提取
 * @author shiyuping
 */
public class ExcelWatermarkExtractMain {

    public static void main(String[] args) {
        // Excel 文件路径
        String ExcelFileName = "/Users/mac/Downloads/客户-集团-演示3501776972074328912.xlsx";
        // 图片路径
        String pictureFileName = "/Users/mac/Downloads/Excel截图.png";
        AbstractWatermarkExtractHandler extractHandler = new ExcelWatermarkExtractHandler();
        //AbstractWatermarkExtractHandler extractHandler = new PictureWatermarkExtractHandler();
        String extract = extractHandler.extract(new File(ExcelFileName));
        System.out.println(extract);
    }
}
