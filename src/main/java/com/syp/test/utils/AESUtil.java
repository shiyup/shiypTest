package com.syp.test.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

import java.nio.charset.StandardCharsets;

/**
 * Created by shiyuping on 2024/5/6 2:52 PM
 *
 * @author shiyuping
 */
public class AESUtil {

    private static final String KEY = "8aj4rsa4v1f5f97b";


    public static String encrypt(String content) {
        AES aes = SecureUtil.aes(KEY.getBytes(StandardCharsets.UTF_8));
        return aes.encryptBase64(content);
    }

    public static String decrypt(String content) {
        AES aes = SecureUtil.aes(KEY.getBytes(StandardCharsets.UTF_8));
        return aes.decryptStr(content);
    }
}
