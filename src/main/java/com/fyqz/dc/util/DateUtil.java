package com.fyqz.dc.util;

import com.fyqz.dc.common.Constants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:[一些日期使用方法]
 * @fileName：com.fyqz.dc.util.DateUtil
 * @createTime: 2014-3-24下午4:13:34
 * @creater:[shihaiyang]
 * @editTime：2014-3-24下午4:13:34
 */
public class DateUtil {


    /**
     * 取得现在时间.
     * 
     * @return 现在时间
     */
    public static String getCurrentSqlTimestampString() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormat.DEFUALT_TIMESTAMPE_FORMAT);
        return sdf.format(today);
    }

    /**
     * 将传入的时间转换为指定时间格式字符串.
     * 
     * @param time 传入时间
     * @param dateFormat 时间格式
     * @return 时间字符串
     */
    public static String formatTimeStamp(Timestamp time, String dateFormat) {
        if (time == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(time);
    }

    /**
     * 取得现在时间.
     * 
     * @return 现在时间
     */
    public static Timestamp getCurrentSqlTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 取得今天日期字符串.
     * 
     * @return 今天日期字符串
     */
    public static String getCurrentDay() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormat.DEFALT_DATE_FORMAT);
        return sdf.format(today);
    }

    /**
     * 将传入的时间转换为默认时间格式字符串.
     * 
     * @param time 传入时间
     * @return 时间字符串
     */
    public static String formatTimeStampDefualt(Timestamp time) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormat.DEFUALT_TIMESTAMPE_FORMAT);
        return sdf.format(time);
    }

    /**
     * 将传入日期转换为默认格式字符串.
     * 
     * @param date 传入日期
     * @return 日期字符串
     */
    public static String formatDateDefault(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormat.DEFALT_DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * 将传入日期转换为指定格式字符串.
     * 
     * @param date 传入日期
     * @param dateFormat 时间格式
     * @return 日期字符串
     */
    public static String formatDateByDateFormate(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }
}
