package com.syp.test.easyexcel.watermark;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.syp.test.easyexcel.model.ExcelDataEntity;
import com.syp.test.easyexcel.util.TextBlindWatermarkUtil;

import java.util.Map;

/**
 * Created by shiyuping on 2024/4/25 5:56 PM
 * excel水印提取
 * @author shiyuping
 */
public class ExcelWatermarkExtractMain {

    public static void main(String[] args) {
        // Excel 文件路径
        String fileName = "/Users/mac/Downloads/test_watermark2.xlsx";
        EasyExcel.read(fileName, ExcelDataEntity.class, new AnalysisEventListener<ExcelDataEntity>() {
            @Override
            public void invoke(ExcelDataEntity data, AnalysisContext context) {

            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }

            @Override
            public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                System.out.println("headMap: " + headMap);
                for (Integer integer : headMap.keySet()) {
                    String head = headMap.get(integer);
                    String extract = TextBlindWatermarkUtil.extract(head);
                    System.out.println("提取到到水印: " + extract);
                    return;
                }

            }
        }).sheet().doRead();
    }
}
