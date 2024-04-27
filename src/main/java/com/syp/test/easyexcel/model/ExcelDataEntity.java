package com.syp.test.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;

@Data
@HeadStyle(verticalAlignment = VerticalAlignmentEnum.CENTER, horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@HeadRowHeight(20)
public class ExcelDataEntity {
    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "年龄")
    private int age;

    @ExcelProperty(value = "手机号")
    private String phoneNumber;

    @ExcelProperty(value = "住址")
    private String address;

    @ExcelProperty(value = "生日")
    private String birthday;
}
