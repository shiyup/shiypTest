package com.syp.test.easyexcel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * @Author shiyuping
 * @Date 2021/10/18 10:42
 */
public class EasyExcelExport {

    public static void main(String[] args) throws Exception{
        String fileName = "D:\\shiyuping\\excel\\t_pharmacy_store_goods.xlsx";
       /* EasyExcel.read(fileName, DrugData.class, new DrugDataUploadListener())
                .headRowNumber(1)
                .sheet().doRead();*/
        Long goodId = 100000000001L;
        Long skuId = 200000000001L;
        String storeCode = "S00000001";
        String merchantCode = "M000000001";
        String sqlTemp = "INSERT INTO `t_pharmacy_store_goods` (`standard_id`, `goods_id`, `store_code`, `merchant_code`, `common_name`, `goods_name`, `producer`, " +
                "`approval_num`, `drug_type`, `category_array`, `first_category_name`, `return_rule`, `is_cold_chain`, " +
                "`goods_image`, `sku_id`, `spec`, `market_price`, `sale_price`, `cost_price`, `stock`, `sale_unit`, " +
                "`third_code`, `audit_status`, `is_sale`, `is_deleted`, `creator`, `remark`) VALUES " +
                "({standard_id}, {goods_id}, '{store_code}', '{merchant_code}', '{common_name}', '{goods_name}', '{producer}', '{approval_num}', {drug_type}, '{category_array}', '{first_category_name}', {return_rule}, {is_cold_chain}, " +
                "{goods_image}, {sku_id}, '{spec}', {market_price}, {sale_price}, {cost_price}, {stock}, '{sale_unit}', '{third_code}', {audit_status}, {is_sale}, 0, 'system', '人工手动导入-jingtian-2021-10-09');";

        ExcelReader excelReader = ExcelUtil.getReader(fileName);
        List<DrugData> drugData = excelReader.read(0, 1, DrugData.class);
        if (CollectionUtil.isNotEmpty(drugData)){
            File file = new File("D:\\shiyuping\\excel\\输出sql.sql");
            if(file.exists()) {
                file.delete();
                System.out.println("删除sql文件成功");
            }
            FileWriter fw = new FileWriter("D:\\shiyuping\\excel\\输出sql.sql", true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (DrugData data : drugData) {
                String sql = sqlTemp.replace("{standard_id}", data.getStandardId())
                        .replace("{goods_id}", goodId + 1 + "")
                        .replace("{store_code}", storeCode)
                        .replace("{merchant_code}", merchantCode)
                        .replace("{common_name}", data.getCommonName())
                        .replace("{goods_name}", data.getGoodsName())
                        .replace("{producer}", data.getProducer())
                        .replace("{approval_num}", data.getApprovalNum())
                        .replace("{drug_type}", data.getDrugType())
                        .replace("{category_array}", data.getCategoryArray())
                        .replace("{first_category_name}", data.getFirstCategoryName())
                        .replace("{return_rule}", data.getReturnRule())
                        .replace("{is_cold_chain}", data.getIsColdChain())
                        .replace("{goods_image}", data.getGoodsImage())
                        .replace("{sku_id}", skuId + 1 + "")
                        .replace("{spec}", data.getSpec())
                        .replace("{market_price}", data.getMarketPrice())
                        .replace("{sale_price}", data.getSalePrice())
                        .replace("{cost_price}", data.getCostPrice())
                        .replace("{stock}", data.getStock())
                        .replace("{sale_unit}", data.getSaleUnit())
                        .replace("{third_code}", data.getThirdCode())
                        .replace("{audit_status}", "2")
                        .replace("{is_sale}", "1");
                bw.append(sql);
                bw.append("\n");
            }
            bw.close();
            fw.close();
            System.out.println("sql文件生成成功-----------------------------------------------------");
        }

    }
}