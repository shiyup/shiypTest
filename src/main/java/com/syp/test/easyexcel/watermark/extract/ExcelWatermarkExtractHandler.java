package com.syp.test.easyexcel.watermark.extract;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.syp.test.easyexcel.util.TextBlindWatermarkUtil;
import com.syp.test.easyexcel.watermark.enums.WatermarkTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * Created by shiyuping on 2024/5/7 10:51 AM
 *
 * @author shiyuping
 */
@Component
@Slf4j
public class ExcelWatermarkExtractHandler extends AbstractWatermarkExtractHandler{


    @Override
    public String extract(File file) {
        String watermark = "";
        EasyExcel.read(file, new AnalysisEventListener<Map<Integer, String>>() {
                    @Override
                    public void invoke(Map<Integer, String> data, AnalysisContext context) {
                        int rouNumber = context.readRowHolder().getRowIndex() + 1;
                        System.out.println("第"+ rouNumber+ "行，数据: " + data);
                    }
                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                    }
                    @Override
                    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                        log.info("表头信息: {}", headMap);
                        for (Integer integer : headMap.keySet()) {
                            String head = headMap.get(integer);
                            log.info("提取表头: {}", head);
                            String extract = TextBlindWatermarkUtil.extract(head);
                            log.info("表头提取到到水印: {}", extract);
                            return;
                        }
                    }
                })
                .sheet()
                .doRead();
        return watermark;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlerHashMap.put(WatermarkTypeEnum.EXCEL, this);
    }
}
