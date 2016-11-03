package com.zok.art.zhihu.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUti {
    /**
     * 关闭流
     */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                LogUtil.getLogUtil(IOUti.class).e(e.getMessage());
            }
        }
        return true;
    }
}
