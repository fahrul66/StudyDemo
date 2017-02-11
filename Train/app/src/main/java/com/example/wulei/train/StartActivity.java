package com.example.wulei.train;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏幕
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        //handle延迟发送信息给主线程处理
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                //销毁
                finish();
            }
        }, 1500);
    }
}
