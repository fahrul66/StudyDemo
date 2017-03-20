package com.example.wulei.demotest;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TestMD extends AppCompatActivity {
    private Toolbar toolbar;
    private CoordinatorLayout cl;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_test_md);
        //设置为action bar
        toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        //swipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sfl);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //设置progressbar的背景色
//                swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLUE);
                //设置progress bar的颜色
                swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,android.R.color.holo_red_light,android.R.color.black);
                //一秒钟之后，关闭progressbar
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //设置不能刷新，progress bar不出现
//                    swipeRefreshLayout.v
                        //设置progress bar消失
                      swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
    }
}
