package com.syp.test.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.util.StringUtils;

/**
 * created by shiyuping on 2019/1/28
 */
public class PoiUtil {
    /**
     * 获取单元格值（字符串）
     * @param cell
     * @return
     */
    public static String getStrFromCell(Cell cell){
        if(cell==null){
            return null;
        }
        //读取数据前设置单元格类型
        cell.setCellType(CellType.STRING);
        String strCell =cell.getStringCellValue();
        if(strCell!=null){
            strCell = strCell.trim();
            if(StringUtils.isEmpty(strCell)){
                strCell=null;
            }
        }
        return strCell ;
    }
}
