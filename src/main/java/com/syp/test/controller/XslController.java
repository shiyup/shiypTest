package com.syp.test.controller;

import com.syp.test.entity.Drug;
import com.syp.test.utils.PoiUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by shiyuping on 2019/1/28
 */
@RestController
@RequestMapping("/api")
public class XslController {
    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";
    private List<Drug> lists;

    /**
     * 读取Excel文件
     *
     *
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map readDoctorExcel(@RequestParam("file") MultipartFile file) {
        Map<String,Object> map = new HashMap<>();
        if(file == null){
            map.put("code",500);
            map.put("msg","file is required");
            System.out.println("file is required");
            return map;
        }
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            map.put("code",500);
            map.put("msg",e.getMessage());
            System.out.println(e);
            return map;
        }
        if (is == null) {
            map.put("code",500);
            map.put("msg","InputStream is required");
            System.out.println("InputStream is required");
            return map;
        }
        //获得用户上传工作簿
        Workbook workbook = null;
        //获取文件的名字
        String originalFilename = file.getOriginalFilename();
        try {
            if (originalFilename.endsWith(SUFFIX_2003)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (originalFilename.endsWith(SUFFIX_2007)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
        } catch (Exception e) {
            map.put("code",500);
            map.put("msg","上传文件格式有问题");
            System.out.println("上传文件格式有问题");
            return map;
        }
        Sheet sheet = workbook.getSheetAt(0);
        Integer total = sheet.getLastRowNum();
        if (total == null || total <= 0) {
            System.out.println( "data is required");
        }

        List<Drug> list = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= total; rowIndex++) {
            Row row = sheet.getRow(rowIndex);//循环获得每个行
            Drug drug = new Drug();
            drug.setDrugId(PoiUtil.getStrFromCell(row.getCell(0)));
            drug.setDrugName(PoiUtil.getStrFromCell(row.getCell(1)));
            drug.setSaleName(PoiUtil.getStrFromCell(row.getCell(2)));
            drug.setDrugSpec(PoiUtil.getStrFromCell(row.getCell(3)));
            drug.setProducer(PoiUtil.getStrFromCell(row.getCell(4)));
            drug.setPack(PoiUtil.getStrFromCell(row.getCell(5)));
            drug.setUnit(PoiUtil.getStrFromCell(row.getCell(6)));
            drug.setDrugType(PoiUtil.getStrFromCell(row.getCell(7)));
            drug.setDrugClass(PoiUtil.getStrFromCell(row.getCell(8)));
            /*drug.setUseDoseUnit(PoiUtil.getStrFromCell(row.getCell(10)));*/
            list.add(drug);
        }
        map.put("code",200);
        map.put("data",list);
        map.put("count",list.size());
        lists = new ArrayList<>(list);
        return map;
    }

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public Map readDoctorExcel(@RequestParam("page")int page,@RequestParam("limit")int limit) {
        Map<String,Object> map = new HashMap<>();
        List<Drug> sList = new ArrayList<>();
        int i = (page-1)*limit;
        int total = i+limit;
        for (;i<total;i++){
            if (i<lists.size()){
                sList.add(lists.get(i));
            }
        }
        map.put("code",0);
        map.put("data",sList);
        map.put("msg","");
        map.put("count",lists.size());
        return map;
    }
}
