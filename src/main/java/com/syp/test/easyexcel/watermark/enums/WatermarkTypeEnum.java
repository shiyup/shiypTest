package com.syp.test.easyexcel.watermark.enums;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Created by shiyuping on 2024/5/7 10:03 AM
 *
 * @author shiyuping
 */
@Getter
@AllArgsConstructor
public enum WatermarkTypeEnum {

    /***/
    OTHER("other", "其他", null),
    PICTURE("picture", "图片水印", ListUtil.of("png", "jpg", "jpeg")),
    TEXT("text", "文本水印", ListUtil.of("txt")),
    EXCEL("excel", "excel水印", ListUtil.of("xls", "xlsx")),
    ;


    private final String type;

    private final String name;

    private final List<String> suffixList;


    public static WatermarkTypeEnum getBySuffix(String suffix){
        return ListUtil.of(values()).stream()
                .filter(watermarkTypeEnum -> watermarkTypeEnum.getSuffixList().contains(suffix))
                .findFirst().orElse(OTHER);
    }
}
