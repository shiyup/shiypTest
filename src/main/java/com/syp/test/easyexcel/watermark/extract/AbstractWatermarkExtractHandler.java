package com.syp.test.easyexcel.watermark.extract;

import com.syp.test.easyexcel.watermark.enums.WatermarkTypeEnum;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shiyuping on 2024/5/7 9:54 AM
 *
 * @author shiyuping
 */
public abstract class AbstractWatermarkExtractHandler implements InitializingBean {

    protected static Map<WatermarkTypeEnum, AbstractWatermarkExtractHandler> handlerHashMap = new HashMap<>();

    public static AbstractWatermarkExtractHandler getExtractHandler(String suffix) {
        return handlerHashMap.get(WatermarkTypeEnum.getBySuffix(suffix));
    }

    public static AbstractWatermarkExtractHandler getExtractHandler(WatermarkTypeEnum typeEnum) {
        return handlerHashMap.get(typeEnum);
    }

    /**
     * 提取水印
     * @param file 文件
     * @return
     */
    public abstract String extract(File file);
}
