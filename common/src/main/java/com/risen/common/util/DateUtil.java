package com.risen.common.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 15:30
 */
public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    //为了加快速度，提升性能,提前生成对象
    public static ConcurrentHashMap<String, DateTimeFormatter> FORMATMAP = new ConcurrentHashMap<>();

    static {
        DateTimeFormatter DATE_TIME_FORMATTER9 = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        FORMATMAP.put(YYYY_MM_DD_HH_MM_SS, DATE_TIME_FORMATTER9);
    }

    public static String parseDate(Long time, String pattern) {
        String d = null;
        try {
            DateTimeFormatter format = FORMATMAP.get(pattern);
            LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time / 1000L, 0, ZoneOffset.ofHours(8));
            d = dateTime.format(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

}
