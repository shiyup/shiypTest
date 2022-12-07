package com.syp.test.wps.convert;

/**
 * @Author shiyuping
 * @Date 2022/12/6 19:29
 */
public class Convert {

    private static final String DOC_URL = "https://xlian-oss-public.oss-cn-hangzhou.aliyuncs.com/dev/test.doc";

    private static final String PPT_URL = "https://xlian-oss-public.oss-cn-hangzhou.aliyuncs.com/dev/test.pptx";


    public static void main(String[] args) throws InterruptedException {
        String taskId = "test123";
        WpsDocumentFacade facade = new WpsDocumentFacade();
        facade.fileConvert(taskId, DOC_URL,"test文档转pdf.pdf","pdf");
        Thread.sleep(1000L);
        System.out.println(facade.recordWPSConvertResult(taskId));
    }
    
    
}
