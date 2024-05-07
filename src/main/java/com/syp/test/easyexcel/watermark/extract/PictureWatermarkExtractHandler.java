package com.syp.test.easyexcel.watermark.extract;

import com.syp.test.easyexcel.watermark.enums.WatermarkTypeEnum;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by shiyuping on 2024/5/7 10:52 AM
 *
 * @author shiyuping
 */
@Component
public class PictureWatermarkExtractHandler extends AbstractWatermarkExtractHandler{
    @Override
    public String extract(File file) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlerHashMap.put(WatermarkTypeEnum.PICTURE, this);
    }
}
