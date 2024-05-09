package com.syp.test.easyexcel.util;

/**
 * Created by shiyuping on 2024/4/25 9:07 PM
 *
 * @author shiyuping
 */

import com.google.common.collect.ImmutableMap;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class TextBlindWatermarkUtil {

    private final List<String> allChrWmHex = Arrays.asList("202C", "202D", "200B", "200C", "200D", "FEFF", "200E", "200F");

    //private final static Map<String, Character> bit2charDict = ImmutableMap.of("0", '\u200E', "1", '\u200F');
    //private final static Map<String, Character> bit2charDict = ImmutableMap.of("0", '\u200A', "1", '\u2009');
    private final static Map<String, Character> bit2charDict = ImmutableMap.of("0", '\u202C', "1", '\u202D');
    private final static Map<Character, String> char2bitDict = ImmutableMap.of('\u202C', "0", '\u202D', "1");


    public static String getWm(String watermark) {
        byte[] watermarkBytes = watermark.getBytes();
        StringBuilder wmBin = new StringBuilder();
        for (byte b : watermarkBytes) {
            // 将字节转换为一个8位的二进制字符串，并去掉前导零
            String binaryString = Integer.toBinaryString(0xFF & b);
            // 用前导零填充至8位
            binaryString = String.format("%8s", binaryString).replace(' ', '0');
            wmBin.append(binaryString);
        }
        String wmBinStr = wmBin.toString();
        System.out.println("8位二进制数:" + wmBinStr);
        //再把二进制数字替换成零宽字符
        StringBuilder wmStr = new StringBuilder();
        char[] chars = wmBinStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            wmStr.append(bit2charDict.get(String.valueOf(chars[i])));
        }
        return wmStr.toString();
    }

    public static String embed(String text, String watermark) {
        String textWithoutWatermark = removeWatermark(text);
        String wm = getWm(watermark);
        //随机切割文本，中间加入水印
        int idx = new Random().nextInt(text.length());
        System.out.println(idx);
        return textWithoutWatermark.substring(0, idx) + wm + textWithoutWatermark.substring(idx);
        //加入到最后
        //return textWithoutWatermark + wm;
    }

    public static String extract(String textEmbed){
        int idxLeft = -1, idxRight = -1;

        for (int idx = 0; idx < textEmbed.length(); idx++) {
            char c = textEmbed.charAt(idx);
            //转为16进制编码
            //String hex = Integer.toHexString(c);
            //System.out.println(hex);
            if (char2bitDict.containsKey(c)) {
                if (idxLeft == -1) {
                    idxLeft = idx;
                }
            } else {
                if (idxLeft != -1) {
                    idxRight = idx;
                    break;
                }
            }
        }

        if (idxRight == -1) {
            idxRight = textEmbed.length();
        }
        if (idxLeft == -1) {
            //无水印
            return "";
        }

        StringBuilder wmExtractBin = new StringBuilder();
        for (int idx = idxLeft; idx < idxRight; idx++) {
            char c = textEmbed.charAt(idx);
            wmExtractBin.append(char2bitDict.get(c));
        }
        String wmExtractBinStr = wmExtractBin.toString();
        System.out.println(wmExtractBinStr);
        return new String(convertBinaryToBytes(wmExtractBinStr), StandardCharsets.UTF_8);
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

    public static String removeWatermark(String textEmbed) {
        return textEmbed.replaceAll(Character.toString(bit2charDict.get("0")), "")
                .replaceAll(Character.toString(bit2charDict.get("1")), "");
    }

    public static void main(String[] args) {
        String originalText = "The text to watermark";
        String watermark = "123456";

        try {
            String embeddedText = embed(originalText, watermark);
            System.out.println("Embedded Text: " + embeddedText);
            String extractedWatermark = extract(embeddedText);
            System.out.println("Extracted Watermark: " + extractedWatermark);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
