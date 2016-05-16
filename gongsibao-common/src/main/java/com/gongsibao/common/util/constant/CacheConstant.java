package com.gongsibao.common.util.constant;

import java.io.Serializable;

/**
 * Created by wk on 2016/4/18.
 */
public class CacheConstant implements Serializable {
    private static final long serialVersionUID = -2557603706993203750L;

    /**
     * 缓存时间常量
     */
    /*秒*/
    public static final int TIME_ONE_SECOND = 1;                    //1秒
    public static final int TIME_TEN_SECOND = 10 * TIME_ONE_SECOND;      //10秒

    /*分*/
    public static final int TIME_HALF_MINUTE = 30 * TIME_ONE_SECOND;     //半分钟
    public static final int TIME_ONE_MINUTE = 60 * TIME_ONE_SECOND;      //1分钟
    public static final int TIME_TWO_MINUTE = 120 * TIME_ONE_SECOND;      //1分钟
    public static final int TIME_FIVE_MINUTES = 5 * TIME_ONE_MINUTE;     //5分钟
    public static final int TIME_TEN_MINUTES = 10 * TIME_ONE_MINUTE;     //10分钟

    /*小时*/
    public static final int TIME_HALF_HOUR = 30 * TIME_ONE_MINUTE;   //半小时
    public static final int TIME_ONE_HOUR = 60 * TIME_ONE_MINUTE;    //1小时
    public static final int TIME_THREE_HOURS = 3 * TIME_ONE_HOUR;    //3小时
    public static final int TIME_TEN_HOURS = 10 * TIME_ONE_HOUR;     //10小时

    /*天*/
    public static final int TIME_HALF_DAY = 12 * TIME_ONE_HOUR;      //半天
    public static final int TIME_ONE_DAY = 24 * TIME_ONE_HOUR;       //1天
    public static final int TIME_TWO_DAYS = 2 * TIME_ONE_DAY;        //2天
    public static final int TIME_ONE_WEEK = 7 * TIME_ONE_DAY;        // 7天
    public static final int TIME_ONE_MONTH = 30 * TIME_ONE_DAY;      //30天
    public static final int TIME_ONE_YEAR = 365 * TIME_ONE_DAY;      //365天

    public static final String LOGIN_KEY = "login_";

    /* 字典缓存key */
    public static final String DIC_KEY = "GSB_DIC_KEY";

    /* 组织机构缓存key*/
    public static final String ORGANIZATION_KEY = "GSB_ORGANIZATION_KEY";

    /* 登录次数 */
    public static final String LOGIN_CAPTCHA_TIMES = "LOGIN_CAPTCHA_TIMES_";



}
