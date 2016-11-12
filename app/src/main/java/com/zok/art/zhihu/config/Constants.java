package com.zok.art.zhihu.config;

import com.zok.art.zhihu.utils.SPUtil;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class Constants {
    /**
     * debug调试模式，所以只在debug模式下使用的功能使用该变量进行判断
     */
    public static final boolean DEBUG = true;

    /**
     * 闪屏页展示的图片的名称
     */
    public static final String SPLASH_IMAGE_NAME = "splash_image_name";

    /**
     * 闪屏页图片版权信息
     */
    public static final String SPLASH_IMAGE_AUTHOR = "splash_image_author";

    /**
     * 闪屏页图片链接
     */
    public static final String SPLASH_IMAGE_URL = "splash_image_url";

    /**
     * SP存储的所有参数的文件名称
     */
    public static final String SHARED_PREFER_FILE = "shared_prefer_file";

    /**
     * 页面跳转传递的参数的键值
     */
    public static final String EXTRA_INIT_PARAMS = "extra_init_params";

    /**
     * 日间/夜间模式存储的键值
     */
    public static final String DAY_NIGHT_MODE = "day_night_mode";

    // ------------------------设置页面参数-----------------------------------------
    /**
     * 首页展示的图片来源键值
     */
    public static final String SPLASH_IMAGE_ORIGIN = "splash_image_origin";

    /**
     * 知乎首页
     */
    public static final int ZHIHU_SPLASH = 0;

    /**
     * bing首页
     */
    public static final int BIND_SPLASH = 1;

    /**
     * 自动离线下载
     */
    public static final String SETTING_AUTO_DOWNLOAD = "auto_download";

    /**
     * 无图模式
     */
    public static final String SETTING_NO_IMAGE = "no_image";

    /**
     * 打字号
     */
    public static final String SETTING_BIG_FONT = "big_font";

    /**
     * 推送消息
     */
    public static final String SETTING_PUSH_MESSAGE = "push_message";

    /**
     * 点评分享到微博
     */
    public static final String SETTING_SHARE_SINA_WEIBO = "share_sina_weibo";

}
