package com.zok.art.zhihu.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class HtmlUtil {

    //css样式,隐藏header
    private static final String HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>";

    //css style tag,需要格式化
    private static final String NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\"/>";

    // js script tag,需要格式化
    private static final String NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>";

    public static final String MIME_TYPE = "text/html; charset=utf-8";

    public static final String ENCODING = "utf-8";


    private HtmlUtil() {

    }

    private static String createCssTag(String url) {
        return String.format(NEEDED_FORMAT_CSS_TAG, url);
    }


    private static String createCssTag(List<String> urls) {
        if(urls == null || urls.size() == 0) return " ";
        final StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(createCssTag(url));
        }
      return sb.toString();
    }

    private static String createJsTag(String url) {
        return String.format(NEEDED_FORMAT_JS_TAG, url);
    }


    private static String createJsTag(List<String> urls) {
        if(urls == null || urls.size() == 0) return " ";
        final StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(createJsTag(url));
        }
        return sb.toString();
    }

    public static String createHtmlData(String html, List<String> cssList, List<String> jsList) {
        final String css = HtmlUtil.createCssTag(cssList);
        final String js = HtmlUtil.createJsTag(jsList);
        return css.concat(HIDE_HEADER_STYLE).concat(html).concat(js);
    }
}
