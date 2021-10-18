package com.syp.test.easyexcel;

import lombok.Data;

/**
 * @Author shiyuping
 * @Date 2021/10/18 10:43
 */
@Data
public class DrugData {

    private String standardId;

    private String storeCode;

    private String merchantCode;

    /**
     * 通用名称
     */
    private String commonName;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 厂家
     */
    private String producer;

    /**
     * 批准文号/进口药品注册证号
     */
    private String approvalNum;

    private String drugType;

    private String categoryArray;

    private String firstCategoryName;

    private String goodsImage;


    /**
     * 规格
     */
    private String spec;

    /**
     * 市场价
     */
    private String marketPrice;

    /**
     * 销售价
     */
    private String salePrice;

    /**
     * 成本价
     */
    private String costPrice;


    /**
     * 是否需冷链运输 0否 1是
     */
    private String isColdChain;

    /**
     * 是否支持7天无理由退换
     * 退换规则0-不支持七天无理由退换，1-支持七天无理由退货
     */
    private String returnRule;

    private String stock;

    /**
     * 销售单位
     */
    private String saleUnit;

    private String thirdCode;
}
