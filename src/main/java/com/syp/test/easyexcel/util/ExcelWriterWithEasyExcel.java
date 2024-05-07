package com.syp.test.easyexcel.util;

import com.alibaba.excel.EasyExcel;
import com.syp.test.easyexcel.watermark.handler.ExcelWatermarkHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName ExcelWriterWithEasyExcel
 * @Description 利用EasyExcel写Excel文件
 * @Version 1.0
 **/
public class ExcelWriterWithEasyExcel {

    /**
     * 利用EasyExcel写Excel文件
     *
     * @param excelPathName     excel文件路径
     * @param content           水印内容
     * @param clazz             数据类
     * @param dataList          数据
     * @throws IOException
     */
    public static void WriteExcelWithEasyExcel(String excelPathName, String content,Class<?> clazz, List<?> dataList) throws IOException {
        FileOutputStream excelWriteStream = new FileOutputStream(excelPathName);
        EasyExcel.write(excelPathName, clazz)
                .inMemory(true)
                .registerWriteHandler(new ExcelWatermarkHandler(content, dataList.size()))
                //自适应列宽
                //.registerWriteHandler(new CustomLongestMatchColumnWidthStyleStrategy())
                //设置sheet名称
                .sheet("Sheet1").doWrite(dataList);
        excelWriteStream.flush();
        excelWriteStream.close();
    }
}
