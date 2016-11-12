package com.zok.art.zhihu.utils;

import android.text.Html;
import android.text.Spanned;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public static Spanned getHtml(int id) {
        return getHtml(AppUtil.getString(id));
    }

    public static Spanned getHtml(String html) {
        return Html.fromHtml(html);
    }

    public static boolean writeFileFromString(File file, String content, boolean append) {
        if (file == null || content == null) return false;
        if (!createOrExistsFile(file)) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.close(bw);
        }
    }

    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
