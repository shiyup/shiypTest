package com.syp.test.easyexcel.watermark;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

import java.nio.charset.StandardCharsets;

/**
 * Created by shiyuping on 2024/4/25 7:02 PM
 *
 * @author shiyuping
 */
public class ExcelWatermarkGenerateMain2 {

    public static void main(String[] args) {
        String str = "12345678";
        //密钥
        String key = RandomUtil.randomString(16);
        System.out.println(key);

        AES aes = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
        System.out.println(aes.encryptHex(str));

        AES aes2 = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
        System.out.println(aes2.decryptStr(aes.encryptHex(str)));

        //将字符串转换为8位二进制
        //String binaryStr = convertBytesToBinary(str.getBytes(StandardCharsets.UTF_8));
        //System.out.println(binaryStr);
        //二进制数据转换为字节数组
        //byte[] bytes = convertBinaryToBytes(binaryStr);
        //System.out.println(new String(bytes, StandardCharsets.UTF_8));

    }

    public static String convertBytesToBinary(byte[] bytes) {
        StringBuilder binaryStringBuilder = new StringBuilder();

        for (byte b : bytes) {
            // 将字节转换为一个8位的二进制字符串，并去掉前导零
            String binaryString = Integer.toBinaryString(0xFF & b);
            // 用前导零填充至8位
            binaryString = String.format("%8s", binaryString).replace(' ', '0');
            binaryStringBuilder.append(binaryString);
        }

        return binaryStringBuilder.toString();
    }

    public static byte[] convertBinaryToBytes(String binaryStr) {
        // 确保二进制字符串长度为8的倍数
        binaryStr = ensureMultipleOf8(binaryStr);
        System.out.println(binaryStr);
        int length = binaryStr.length() / 8;
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            // 提取8位二进制字符串
            String byteBinaryStr = binaryStr.substring(i * 8, i * 8 + 8);
            // 将二进制字符串转换为整数
            int intValue = Integer.parseInt(byteBinaryStr, 2);
            // 将整数转换为字节
            bytes[i] = (byte) intValue;
        }

        return bytes;
    }

    private static String ensureMultipleOf8(String binaryStr) {
        int remainder = binaryStr.length() % 8;
        if (remainder != 0) {
            // 补零操作
            return binaryStr.substring(0, remainder).replace("1", "0") + binaryStr;
        }
        return binaryStr;
    }


}
