package com.wulei.runner.activity;

import android.content.Intent;
import android.os.Handler;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;

/**
 * Created by wule on 2017/03/21.
 * 是程序的启动画面
 */

public class StartUpActivity extends BaseActivity {


    @Override
    protected void hideShowFragment() {

    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_startup;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //延迟跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(StartUpActivity.this,MainActivity.class));
            }
        },2000);
    }
}
