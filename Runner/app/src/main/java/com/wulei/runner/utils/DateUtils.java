package com.wulei.runner.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.b.I;

/**
 * Created by wule on 2017/04/10.
 */

public class DateUtils {

    /**
     * 将时间转换成yyyy-MM-dd的字符串
     *
     * @param mills 毫秒
     * @return
     */
    public static String convertToStr(long mills) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String res = sdf.format(new Date(mills));
        return res;
    }
    /**
     * 将时间转换成yyyy-MM-dd的字符串
     *
     * @param mills 毫秒
     * @return
     */
    public static String convertToStrAll(long mills) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String res = sdf.format(new Date(mills));
        return res;
    }
    /**
     * 将字符串时间转换成long 毫秒
     *
     * @param str
     * @return
     */
    public static long converToMills(String str) {

        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        try {
            Date date = sdf.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            ToastUtil.show("日期解析格式错误！");
        }

        return 0;
    }

    /**
     * 转换成时间
     *
     * @param time
     * @return
     */
    public static String converToStr(float time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String res = sdf.format(new Date((long) time));
        return res;
    }

    /**
     * 字符串转换成秒
     * @param time
     * @return
     */
    public static float strToHour(String time) {
        long t = 0;
        //切分
        String[] split = time.split(":");
        //小时
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        int second = Integer.parseInt(split[2]);

        t = hour * 60 * 60 + minute * 60 + second;
        return t/(60 * 60);
    }
}
