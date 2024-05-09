package com.syp.test.easyexcel.watermark.extract;

import com.syp.test.easyexcel.watermark.enums.WatermarkTypeEnum;
import com.syp.test.utils.OpencvUtil;
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
        File dstFile = new File("/temp/" + file.getName());
        OpencvUtil.light(file.getAbsolutePath(), "/temp/"+ file.getName());
        //todo--上传oss
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlerHashMap.put(WatermarkTypeEnum.PICTURE, this);
    }
}
