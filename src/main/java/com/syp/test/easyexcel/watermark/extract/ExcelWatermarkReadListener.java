package com.syp.test.easyexcel.watermark.extract;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.syp.test.easyexcel.util.TextBlindWatermarkUtil;
import com.syp.test.easyexcel.watermark.enums.ZeroWidthCharEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Created by shiyuping on 2024/5/7 1:43 PM
 *
 * @author shiyuping
 */
@Slf4j
public class ExcelWatermarkReadListener extends AnalysisEventListener<Map<Integer, String>> {

    private String watermark = "";

    /**
     * 表头数量
     */
    private Integer headSize;

    public String getWatermark(){
        return watermark;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        if (StrUtil.isNotEmpty(watermark)) {
            //return;
        }
        int rouNumber = context.readRowHolder().getRowIndex() + 1;
        log.info("第{}行，数据: {}", rouNumber, data);
        //读取表头列后两列
        String content = data.get(headSize + 1);
        if (StrUtil.isNotEmpty(content) && content.contains(ZeroWidthCharEnum.ZERO_WIDTH_CHAR_0.getZeroWidthChar())) {
            String extract = TextBlindWatermarkUtil.extract(content);
            log.info("第{}行，第{}列数据，提取到到水印: {}", rouNumber, headSize + 2, extract);
            watermark = extract;
            //return;
        }
        //读取数据行最后一行后的第二行
        for (int i = 0; i < headSize; i++){
            String value = data.get(i);
            if (StrUtil.isNotEmpty(value) && value.contains(ZeroWidthCharEnum.ZERO_WIDTH_CHAR_0.getZeroWidthChar())){
                String extract = TextBlindWatermarkUtil.extract(value);
                log.info("第{}行，第{}列数据，提取到到水印: {}", rouNumber, i + 1, extract);
                watermark = extract;
                //return;
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("表头信息: {}", headMap);
        headSize = headMap.size();
        for (Integer integer : headMap.keySet()) {
            String head = headMap.get(integer);
            log.info("提取表头: {}", head);
            String extract = TextBlindWatermarkUtil.extract(head);
            log.info("表头提取到到水印: {}", extract);
            watermark = extract;
        }
    }
}
