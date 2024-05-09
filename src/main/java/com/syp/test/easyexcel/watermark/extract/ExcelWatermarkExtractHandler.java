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
        ExcelWatermarkReadListener readListener = new ExcelWatermarkReadListener();
        EasyExcel.read(file, readListener)
                .sheet()
                .doRead();
        return readListener.getWatermark();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlerHashMap.put(WatermarkTypeEnum.EXCEL, this);
    }
}
