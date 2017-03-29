package com.wulei.runner.app;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.wulei.runner.utils.ConstantFactory;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

/**
 * Created by wulei on 2017/1/13.
 */

public class App extends Application {

    public static  Context mAPPContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化操作
        initBaiduMap();
        initBmob();
        //获得系统上下文
        mAPPContext = this;
    }

    /**
     * Bmob后台初始化
     */
    private void initBmob() {
        Bmob.initialize(this, ConstantFactory.BMOB_APPKEY);
    }

    /**
     * 百度地图初始化
     */
    private void initBaiduMap() {
        SDKInitializer.initialize(this);
    }


}
