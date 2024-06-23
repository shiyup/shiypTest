package com.syp.test.easyexcel.watermark.extract;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.syp.test.easyexcel.watermark.enums.WatermarkTypeEnum;
import com.syp.test.utils.OpencvUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * Created by shiyuping on 2024/5/7 10:52 AM
 *
 * @author shiyuping
 */
@Component
public class PictureWatermarkExtractHandler extends AbstractWatermarkExtractHandler{
    @Override
    public String extract(File file) {
        File dstFile = new File("/Users/mac/Downloads/"+ getNewFileName(file.getName()));
        OpencvUtil.light(file.getAbsolutePath(), dstFile.getAbsolutePath());
        return dstFile.getAbsolutePath();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlerHashMap.put(WatermarkTypeEnum.PICTURE, this);
    }

    public String getNewFileName(String fileName) {
        // 主文件名，不包含扩展名
        String prefix = FileNameUtil.getPrefix(fileName);
        // 文件扩展名
        String suffix = FileNameUtil.getSuffix(fileName);
        // 新文件名
        return prefix + "_" + "水印提取" + "." + suffix;
    }
}
