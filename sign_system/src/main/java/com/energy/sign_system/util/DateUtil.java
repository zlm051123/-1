package com.energy.sign_system.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化日期
     */
    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    /**
     * 自定义格式格式化日期
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 判断当前时间是否在课程开始时间之后（用于判断是否迟到）
     */
    public static boolean isLate(Date courseStartTime) {
        if (courseStartTime == null) {
            return false;
        }
        return new Date().after(courseStartTime);
    }

    /**
     * 计算两个日期的分钟差
     */
    public static long getMinuteDiff(Date start, Date end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs((end.getTime() - start.getTime()) / (1000 * 60));
    }
}
