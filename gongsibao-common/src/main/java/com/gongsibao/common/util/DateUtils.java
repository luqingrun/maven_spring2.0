/**
 *
 */
package com.gongsibao.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * 转换日期格式字符串 (yyyy-MM-dd)
     *
     * @param obj
     * @return
     */
    public static String dateStr(Object obj) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(obj);
    }

    /**
     * 转换日期格式字符串 (yyyy-MM-dd HH:mm:ss)
     *
     * @param obj
     * @return
     */
    public static String dateTimeStr(Object obj) {
        if (null == obj) {
            return "";
        }

        if ("0000-00-00 00:00:00".equals(obj.toString())) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(obj);
    }

    /**
     * 转换日期格式字符串
     *
     * @param obj
     * @param pattern
     * @return
     */
    public static String dateStr(Object obj, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(obj);
    }

    /**
     * 将字符串转换成java.util.Date (yyyy-MM-dd)
     *
     * @param str (yyyy-MM-dd)
     * @return
     */
    public static Date strToDate(String str) {
        if (!RegexUtils.isNotDate(str)) {
            throw new RuntimeException("str is not date");
        }
        Date date = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 字符串转换成java.util.Date (yyyy-MM-dd HH:mm:ss)
     *
     * @param str (yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static Date strToDateTime(String str) {
        if (RegexUtils.isNotDate(str)) {
            throw new RuntimeException("str is not datetime");
        }
        Date date = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getMinTime(Date dt) {
        Date dt1 = null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            dt1 = sf.parse(sf.format(dt));
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("date formate error ：" + dt + ".   " + e.getMessage());
        }
        return dt1;
    }

    /**
     * 计算两个日期相差的天数
     *
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static long getDistinceDay(Date beforeDate, Date afterDate) {
        long dayCount = 0;
        dayCount = (afterDate.getTime() - beforeDate.getTime()) / (24 * 60 * 60 * 1000);
        return dayCount;
    }

    /**
     * 计算两个日期相差的年数
     *
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static int getDistinceYear(Date beforeDate, Date afterDate) {
        try {
            String beforeYear = DateFormatUtils.format(beforeDate, "yyyy");
            String afterYear = DateFormatUtils.format(afterDate, "yyyy");
            if (StringUtils.isNotBlank(beforeYear) && StringUtils.isNotBlank(afterYear)) {
                return Integer.parseInt(afterYear) - Integer.parseInt(beforeYear);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 当天到第二天12点的秒数
     *
     * @return
     */
    public static int exipireTime() {
        Calendar curDate = Calendar.getInstance();
        Calendar tommorowDate = new GregorianCalendar(curDate.get(Calendar.YEAR),
                curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE) + 1, 0, 0, 0);
        long timeCap = tommorowDate.getTimeInMillis() - System.currentTimeMillis();
        return (int) timeCap / 1000;
    }

    /**
     * 返回当前时间前指定年数列表
     *
     * @param count
     * @return
     * @author
     */
    public static List<Integer> getBeforeYear(int count) {
        Calendar curDate = Calendar.getInstance();
        int nowYear = curDate.get(Calendar.YEAR);
        List<Integer> yearList = new ArrayList<Integer>();
        for (int i = nowYear; i > (nowYear - count); i--) {
            yearList.add(i);
        }
        return yearList;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static void main(String[] args) {
        try {
            System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd 00:00:00"));
            System.out.println("date is:"+getWeekOfDate(new Date()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
