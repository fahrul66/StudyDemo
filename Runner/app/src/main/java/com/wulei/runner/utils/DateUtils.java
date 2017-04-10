package com.wulei.runner.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
