package com.syp.test.easyexcel.watermark;

import com.syp.test.easyexcel.model.ExcelDataEntity;
import com.syp.test.easyexcel.util.ExcelWriterWithEasyExcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ExcelWatermarkGenerateMain
 * @Description
 * @Version 1.0
 * excel 水印生成
 **/
public class ExcelWatermarkGenerateMain {
    public static void main(String[] args) throws Exception {
        ExcelWriterWithEasyExcel.WriteExcelWithEasyExcel("/Users/mac/Downloads/easyExcel_watermark_test_str2.xlsx",
                "12345678",
                ExcelDataEntity.class,
                generateData());
    }


    private static List<ExcelDataEntity> generateData() {
        List<ExcelDataEntity> excelDataList = new ArrayList<>();
        ExcelDataEntity excelDataEntity;
        for (int i = 1; i <= 20; i++) {
            excelDataEntity = new ExcelDataEntity();
            excelDataEntity.setName("Robert 0" + i);
            excelDataEntity.setAge(i+20);
            excelDataEntity.setPhoneNumber("13932802374");
            excelDataEntity.setAddress("Yuelu Boulevard");
            excelDataEntity.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            excelDataList.add(excelDataEntity);
        }
        return excelDataList;
    }
}
