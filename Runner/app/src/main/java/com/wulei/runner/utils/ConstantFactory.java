package com.wulei.runner.utils;

import android.Manifest;

/**
 * Created by wulei on 2017/1/13.
 */

public class ConstantFactory {
    /**
     * 整个项目的static final 变量
     */
    //bmob,appkey
    public static final String BMOB_APPKEY = "2eba4d486c3384cc5bd0f4079c71d572";
    //fragment的唯一标识
    public static final String TAG_RUN = "run";
    public static final String TAG_MAP = "map";
    public static final String TAG_GOAL = "goal";
    public static final String TAG_RECORD = "record";
    public static final String TAG_NEWS = "news";
    public static final String TAG_RANK = "rank";
    public static final String TAG_SETTING = "setting";

    //需要的权限
    public static final String[] PERMISSION = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    public static final int REQUEST_CODE = 1;

    //数据库名
    public static final String SQL_NAME = "localDB.db";
    public static final String SQL_TABLE_JB = "pedometer";
    public static final String SQL_TABLE_PB = "run";
    public static final int SQL_VERSION = 1;

    //更新数据传递
    public static final int SAVE = 1;
    public static final int INIT = 2;
}
