package com.zok.art.zhihu.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class DateUtil {
    public static Date getBeforeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.CHINA);
        return format1.format(date);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat("MM月dd日 E", Locale.CHINA);
        return format1.format(date);
    }

    public static String formatTime(long timeStamp) {
        DateFormat format = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        return format.format(timeStamp);
    }
}
