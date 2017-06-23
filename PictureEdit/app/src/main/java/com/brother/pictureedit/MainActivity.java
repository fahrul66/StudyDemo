package com.brother.pictureedit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {

    private PhotoView pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化Logger日志库
        Logger.addLogAdapter(new AndroidLogAdapter());

        pv = (PhotoView) findViewById(R.id.imgV);
        pv.setImageResource(R.drawable.yourname);



    }


}
