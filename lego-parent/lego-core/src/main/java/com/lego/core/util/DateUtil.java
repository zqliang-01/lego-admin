package com.lego.core.util;

import cn.hutool.core.date.BetweenFormatter;
import com.lego.core.exception.CoreException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    private static final Date MAX_DATE_TIME = new GregorianCalendar(9999, Calendar.DECEMBER, 30, 23, 59, 59).getTime();

    /**
     * yyyyMM
     */
    public static final String monthPattern = "yyyyMM";
    /**
     * yyyy-MM-dd
     */
    public static final String datePattern = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    /**
     * HH:mm:ss
     */
    public static final String timePattern = "HH:mm:ss";

    public static final int dateTimePatternLength = dateTimePattern.length();
    public static final int datePatternLength = datePattern.length();

    public static String toDateString(Date date1, Date date2) {
        return toDateString(date1, datePattern) + " ~ " + toDateString(date2, datePattern);
    }

    public static String toDateString(Date date) {
        return toDateString(date, datePattern);
    }

    public static String toDateTimeString(Date date) {
        return toDateString(date, dateTimePattern);
    }

    public static String formatString(long time) {
        return new BetweenFormatter(time, BetweenFormatter.Level.MILLISECOND).format();
    }

    public static String toDateString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static Date toDateTime(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        return formatDate(dateTimePattern, dateTime);
    }

    public static Date toDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        return formatDate(datePattern, date);
    }

    public static Date toDate(Date date) {
        if (date == null) {
            return null;
        }
        return formatDate(datePattern, toDateString(date));
    }

    public static Date formatDate(String datePattern, String dateStr) {
        try {
            return new SimpleDateFormat(datePattern).parse(dateStr);
        } catch (ParseException e) {
            throw new CoreException("格式装换日期出错", e);
        }
    }

    public static Date clone(Date date) {
        return new Date(date.getTime());
    }

    public static Date toDate(long date) {
        return new Date(date);
    }

    public static Date currentDateTime() {
        return new Date();
    }

    public static String currentDateTimeString() {
        return toDateTimeString(currentDateTime());
    }

    public static Date currentDate() {
        Date date = currentDateTime();
        return toDate(date);
    }

    public static Date currentDate(Date date) {
        if (date == null || lt(toDate(date), currentDate())) {
            return currentDateTime();
        }
        return date;
    }

    public static Date yesterday() {
        Date date = currentDateTime();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return toDate(c.getTime());
    }

    public static long currentTimeMillis() {
        return currentDateTime().getTime();
    }

    public static Date maxDate() {
        return toDate(MAX_DATE_TIME);
    }

    public static Date addDays(Date date, int amount) {
        return DateUtils.addDays(date, amount);
    }

    public static int daydiff(Date afterDateTime, Date beforeDateTime) {
        Date afterDate = toDate(afterDateTime);
        Date beforeDate = toDate(beforeDateTime);
        long dateTime = afterDate.getTime() - beforeDate.getTime();
        return (int) Math.abs(dateTime / (1000 * 3600 * 24));
    }

    public static boolean equalDate(Date dateOne, Date dateTwo) {
        if (dateOne == null || dateTwo == null) {
            return false;
        }
        return DateUtils.isSameDay(dateOne, dateTwo);
    }

    /**
     * 大于
     */
    public static boolean gt(Date bigDate, Date smallDate) {
        if (bigDate == null || smallDate == null) {
            return false;
        }
        return bigDate.after(smallDate);
    }

    /**
     * 大于等于
     */
    public static boolean ge(Date bigDate, Date smallDate) {
        if (bigDate == null || smallDate == null) {
            return false;
        }
        return bigDate.after(smallDate) || equalDate(bigDate, smallDate);
    }

    /**
     * 小于
     */
    public static boolean lt(Date smallDate, Date bigDate) {
        if (bigDate == null || smallDate == null) {
            return false;
        }
        return smallDate.before(bigDate);
    }

    /**
     * 小于等于
     */
    public static boolean le(Date smallDate, Date bigDate) {
        if (bigDate == null || smallDate == null) {
            return false;
        }
        return smallDate.before(bigDate) || equalDate(bigDate, smallDate);
    }

    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        if (day == 0) {
            return hour + "小时" + min + "分钟";
        }
        // 计算差多少秒//输出结果
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static String getMonthString() {
        return toDateString(currentDateTime(), monthPattern);
    }

}