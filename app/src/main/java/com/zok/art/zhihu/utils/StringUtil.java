package com.zok.art.zhihu.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
    private static final LogUtil log = LogUtil.getLogUtil(StringUtil.class);
    public final static String UTF_8 = "utf-8";

    public static String MD5(String str) {
        String key;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte[] bytes = digest.digest();
            key = bytesToHexString(bytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            key = String.valueOf(str.hashCode());
        }
        return key;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte bt : bytes) {
            String hex = Integer.toHexString(0xFF & bt);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
