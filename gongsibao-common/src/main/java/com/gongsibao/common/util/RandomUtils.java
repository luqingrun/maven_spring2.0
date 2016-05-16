package com.gongsibao.common.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RandomUtils {

    /**
     * 获取随机数组
     *
     * @param topNum 需要返回的随机数数量(无重复)
     * @param num    在此数量上进行随机
     * @return
     */
    public static int[] getRandom(int topNum, int num) {
        if (topNum <= 0 || num <= 0 || topNum > num) {
            return null;
        }
        int result[] = new int[topNum];
        int a[] = new int[num];
        int k, temp;
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        for (int i = 0; i < topNum; i++) {
            k = (int) (Math.random() * (a.length - i));
            temp = a[k];
            a[k] = a[a.length - i - 1];
            a[a.length - i - 1] = temp;
            result[i] = temp;
        }
        return result;
    }

    /**
     * 获取随机数组
     *
     * @param topNum 需要返回的随机数数量(无重复)
     * @param minNum 最小ID
     * @param maxNum 最大ID
     * @return
     */
    public static long[] getRandom(int topNum, long minNum, long maxNum) {
        if (topNum <= 0 || minNum <= 0l || maxNum <= 0l) {
            return null;
        }
        int num = (int) ((maxNum - minNum) + 1);

        long result[] = null;
        if (topNum >= num) {
            result = new long[num];
            for (int i = 0; i < num; i++) {
                result[i] = i + minNum;
            }
        } else {
            int a[] = new int[num];
            for (int i = 0; i < a.length; i++) {
                a[i] = i;
            }
            int k, temp;
            result = new long[topNum];
            for (int i = 0; i < topNum; i++) {
                k = (int) (Math.random() * (a.length - i));
                temp = a[k];
                a[k] = a[a.length - i - 1];
                a[a.length - i - 1] = temp;
                result[i] = temp + minNum;
            }
        }
        return result;
    }

    /**
     * 随机List
     *
     * @param lstData
     * @param count
     * @return
     */
    public static <T> List<T> getRandomList(List<T> lstData, int count) {
        if (lstData == null || lstData.size() < count) {
            return lstData;
        } else {
            List<T> lstResult = new ArrayList<T>(count);
            int[] result = getRandom(count, lstData.size());
            if (result == null) {
                return lstData;
            } else {
                for (int j : result) {
                    lstResult.add(lstData.get(j));
                }
                return lstResult;
            }
        }
    }

    public static String getUniqBillString(String header, int length) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return header + df.format(cal.getTime()) + RandomStringUtils.randomNumeric(length);
    }
}
