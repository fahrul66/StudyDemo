package com.wulei.runner.activity;

import android.app.Activity;

import com.wulei.runner.activity.base.BaseActivity;

/**
 * Created by wule on 2017/03/21.
 * 第一次启动时候的引导页，可以从本地数据库，或者是网络服务器读取数据。
 */

public class GuideActivity extends BaseActivity {
    @Override
    protected void hideShowFragment() {

    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

}
