package com.syp.test.wps.convert;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.HttpHeaders;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Future;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

/**
 * WPS文档处理
 * */
public class WpsDocumentFacade {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**设置WPS对接参数*/
    private final String wpsConvertAppid = "SX20221206UCPCNZ"; //文件转换appid
    private final String wpsConvertAppsecret = "6b06cc2c35e18ef24d2867c5aa66b2fd"; //文件转换appsecret

    /**设置固定的常量*/
    private final String CONTENTTYPE  = "application/json";
    private final String CONVERTAPI = "https://dhs.open.wps.cn/pre/v1/convert" ; //转换请求地址
    private final String QUERYAPI = "https://dhs.open.wps.cn/pre/v1/query" ; //查询请求地址;
    private final String WPSCONVERTCALLBACK = "http://xxxx.xx.xxx:port/xxx/xxx"; //回调地址，WpsCallbackController.convertresult对应的地址，文件转换后的通知地址，需保证可访问；访问不了也没关系，可以再调query接口


    /** 文件转换
     * WPS接口的文件转换
     * @author Lemon
     * @time 2020-09-01
     * 文档： https://open.wps.cn/docs/doc-format-conversion/access-know
     * 文件原格式	转换后格式
     * word			pdf、png
     * excel		pdf、png
     * ppt			pdf
     * pdf			word、ppt、excel
     * @param taskId 任务ID，需唯一
     * @param srcUri 原文件地址 需要转换的文件地址，保证可打开、下载
     * @param fileName 文件名 好象没什么用，转换后下载的文件名都不是这个的。
     * @param exportType 转换的格式，pdf、png等
     * */
    public void fileConvert(String taskId , String srcUri , String fileName , String exportType) {
        try{
            String headerDate = getGMTDate() ; // (new Date()).toString();

            //请求参数
            Map<String, Object> param = Maps.newLinkedHashMap(); //注，使用newLinkedHashMap可让顺序一致，调用API时参数顺序与参与签名时顺序一致。
            param.put("SrcUri", srcUri);
            param.put("FileName", fileName);
            param.put("ExportType", exportType);
            param.put("CallBack", WPSCONVERTCALLBACK);//回调地址，文件转换后的通知地址，需保证可访问；访问不了也没关系，可以再调query接口
            param.put("TaskId", taskId);

            //********计算签名
            //Content-MD5 表示请求内容数据的MD5值，对消息内容（不包括头部）计算MD5值获得128比特位数字，对该数字进行base64编码而得到，如”eB5eJF1ptWaXm4bijSPyxw==”，也可以为空；
            String contentMd5 = getMD5(param) ; //请求内容数据的MD5值

            //Signature = base64(hmac-sha1(AppKey, VERB + “\n” + Content-MD5 + “\n” + Content-Type + “\n” + Date + “\n” + URI))
            //VERB表示HTTP 请求的Method的字符串，可选值有PUT、GET、POST、HEAD、DELETE等；
            String signature = getSignature("POST" , CONVERTAPI , contentMd5 , headerDate) ;//签名url的参数不带请求参数
            String authorization =  "WPS " + wpsConvertAppid + ":" + signature ; //签名
            logger.info("生成签名，contentMd5={} , authorization={}" , contentMd5 , authorization);

            //header参数
            Map<String, String> headers = Maps.newLinkedHashMap();
            headers.put(HttpHeaders.CONTENT_TYPE ,CONTENTTYPE ) ;
            headers.put(HttpHeaders.DATE , headerDate) ;
            headers.put(HttpHeaders.CONTENT_MD5 , contentMd5) ;//文档上是 "Content-Md5"
            headers.put(HttpHeaders.AUTHORIZATION , authorization) ;


            //调用时用json Body数据提交
            // 内容如{"SrcUri":"http://xxx","FileName":"xxx","ExportType":"pdf","CallBack":"http://xxx/v1/3rd/convertresult","TaskId":"abcd1234"};
            String parsStr = JSONUtil.toJsonStr(param);
            logger.info("parsStr = {}" , parsStr) ;
            String result = httpPost(CONVERTAPI ,headers ,  parsStr );

            logger.info("返回结果,result={}" ,  result) ;
            JSONObject dataJson = JSONUtil.parseObj(result);
            logger.info("返回结果，Code={}" , dataJson.get("Code") ) ;

            String code = dataJson.get("Code").toString() ;
            if (code.equals("OK")){
                //成功，做其它业务处理
            }
            else{
                String errorMsg = "文件格式转换失败" ;
                if (dataJson.get("Message") != null){
                    String message = dataJson.get("Message").toString();
                    errorMsg = errorMsg + message;
                }

                //失败，做其它业务处理

            }
        } catch (Exception e) {
            logger.error("fileConvert处理出错，错误={}" , e.getMessage() , e);

        }
    }


    /**
     * 检查WPS文件转换结果
     * 调用WPS格式转换查询接口
     * 可以在回调中执行，也可以用于异步查询结果
     * 返回转换后的文件地址，地址是有时限的，所以需要及时使用或保存，建议保存到自己服务器中，以免因过期问题打开地址失败
     * */
    public String recordWPSConvertResult(String taskId){
        String headerDate = getGMTDate() ; // (new Date()).toString();
        String downUrl = "" ;
        try{
            //请求参数
            Map<String, Object> param = Maps.newLinkedHashMap();
            param.put("TaskId", taskId);
            param.put("AppId", wpsConvertAppid);

            String contentMd5 = getMD5(null) ; //请求内容数据的MD5值，用null作入参

            //********计算签名
            //Content-MD5 表示请求内容数据的MD5值，对消息内容（不包括头部）计算MD5值获得128比特位数字，对该数字进行base64编码而得到，如”eB5eJF1ptWaXm4bijSPyxw==”，也可以为空；
            logger.info("contentMd5={}" , contentMd5);

            String url = QUERYAPI + "?TaskId=" + taskId + "&AppId=" + wpsConvertAppid;
            String signature = getSignature("GET" , url , contentMd5 , headerDate) ;//签名url的参数带请求参数
            String authorization =  "WPS " + wpsConvertAppid + ":" + signature ; //签名
            logger.info("authorization={}" , authorization);

            //header参数
            Map<String, String> headers = Maps.newLinkedHashMap();
            headers.put(HttpHeaders.CONTENT_TYPE ,CONTENTTYPE ) ;
            headers.put(HttpHeaders.DATE , headerDate) ;
            headers.put(HttpHeaders.CONTENT_MD5 , contentMd5) ;//文档上是 "Content-Md5"
            headers.put(HttpHeaders.AUTHORIZATION , authorization) ;

            //调用
            String parsStr = JSONUtil.toJsonStr(param);
            logger.info("parsStr = {}" , parsStr) ;

            String result =httpGet(url , headers) ;
            logger.info("result={}" , result);

            JSONObject dataJson = JSONUtil.parseObj(result);
            logger.info("返回结果，Code={}" , dataJson.get("Code") ) ;

            String code = dataJson.get("Code").toString() ;
            if (code.equals("OK")){
                if (dataJson.get("Urls") != null){ //实际上返回这个参数
                    downUrl = ((JSONArray)dataJson.get("Urls")).toString() ;
                }
                else if (dataJson.get("Url") != null){//文档是返回这个参数
                    downUrl = dataJson.get("Url").toString() ;
                }

                //成功，做其它业务处理
            }
            else{
                String errorMsg = "文件格式转换失败" ;
                if (dataJson.get("Message") != null){
                    String message = dataJson.get("Message").toString();
                    errorMsg = errorMsg + message;
                }

                //失败，做其它业务处理

            }


        } catch (Exception e) {
            logger.error("recordWPSConvertResult处理出错，错误={}" , e.getMessage() , e);

        }

        return downUrl;
    }

    /**
     * 生成时间（header中的Date）
     * */
    private String getGMTDate(){
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'" , Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        String str = sdf.format(cd.getTime());
        return str;
    }

    /**
     * 计算MD5
     * @param  paramMap 参加运算的参数，调转换接口时，参数的顺序需与调用API时一致；调查询接口时，传null
     *
     */
    private String getMD5(Map<String, Object> paramMap) {
        try{
            //String req = paramMap.toString() ;
            String req = "";
            if (paramMap != null){
                req = JSONUtil.toJsonStr(paramMap);
            }


            String md5Value = DigestUtils.md5Hex(req);
            logger.info("请求签名req={}，签名结果md5Value={}" , req , md5Value);
            return md5Value;
        }
        catch (Exception e){
            logger.error("getMD5报错={}" , e.getMessage() , e);
        }

        return "" ;
    }

    //Signature = base64(hmac-sha1(AppKey, VERB + “\n” + Content-MD5 + “\n” + Content-Type + “\n” + Date + “\n” + URI))
    //VERB表示HTTP 请求的Method的字符串，可选值有PUT、GET、POST、HEAD、DELETE等；
    /**
     * 生成签名
     * @param action GET、POST
     * @param url 调用接口的url，转换接口时传入接口地址不带参；查询接口时地址带参数
     * @param contentMd5 通过getMD5方法计算的值
     * @param headerDate 通过getGMTDate方法计算的值
     * */
    private String getSignature(String action , String url , String contentMd5 , String headerDate){
        try{
            String req = getUri(url);
            String signStr = action + "\n" + contentMd5 + "\n" + CONTENTTYPE + "\n" + headerDate + "\n" + req ;
            logger.info("signStr={}" , signStr);

            // 进行hmac sha1 签名
            byte[] bytes = HmacUtils.hmacSha1(wpsConvertAppsecret.getBytes(), signStr.toString().getBytes());
            String sign = encodeBase64String(bytes); //
            return sign;

        }
        catch (Exception e){
            logger.error("getSignature出错，错误={}" , e.getMessage() , e);
        }

        return "";

    }

    /**
     * 取得地址的uri
     * （去掉地址中的域名）
     * */
    private String getUri(String link){
        try{
            URL url = new URL(link);
            String key = url.getPath();
            if (!StringUtils.isEmpty(url.getQuery())){
                key = key + "?"+ url.getQuery();
            }

            //if (key.startsWith("/")){
            //key = key.substring(1 , key.length());
            //}
            return key;
        }
        catch (Exception e){
            logger.error("getUri出错，link={}，错误={}" , link , e.getMessage() ,e);
        }

        return link;
    }


    /**
     * HTTP POST请求
     * 支持 header和body体的提交
     * 在POSTMAN中可模拟，其中Headers为请求中的header参数，body为 raw JSON数据，如：
     *      {"SrcUri":"https://可访问的文档地址","FileName":"xxx.docx","ExportType":"pdf","CallBack":"http://xxx:yyy/wps/convertresult","TaskId":"abcd1234"}
     * */
    public String httpPost(String uri, Map<String, String> headers, String paramsJson) {
        try {
            AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

            io.netty.handler.codec.http.HttpHeaders httpHeaders = new DefaultHttpHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry != null && entry.getKey() != null) {
                    httpHeaders.add(entry.getKey(), entry.getValue());
                }
            }

            Future<Response> response = asyncHttpClient.preparePost(uri).setBody(paramsJson).setHeaders(httpHeaders).execute();
            String result = response.get().getResponseBody();
            logger.info("结果，result={} , data={} , status={} , statustext={}" ,
                    result , response.get().getStatusCode() , response.get().getStatusText() );

            return result;

        } catch (Exception e) {
            logger.error("error uri={}, params={}", uri, paramsJson, e);
            return null;
        }
    }

    /**
     * HTTP GET请求
     * 支持 header
     * 在POSTMAN中可模拟，其中Headers为请求中的header参数，params为请求地址中的参数
     *
     * */
    public String httpGet(String uri, Map<String, String> headers) {
        try {
            AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

            io.netty.handler.codec.http.HttpHeaders httpHeaders = new DefaultHttpHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry != null && entry.getKey() != null) {
                    httpHeaders.add(entry.getKey(), entry.getValue());
                }
            }

            Future<Response> response = asyncHttpClient.prepareGet(uri).setHeaders(httpHeaders).execute();
            String result = response.get().getResponseBody();
            logger.info("结果，result={} , data={} , status={} , statustext={}" ,
                    result , response.get().getStatusCode() , response.get().getStatusText() );

            return result;

        } catch (Exception e) {
            logger.error("error uri={}", uri,  e);
            return null;
        }
    }
}
