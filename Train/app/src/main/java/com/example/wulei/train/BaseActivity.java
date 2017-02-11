package com.example.wulei.train;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

/**
 * Created by wulei on 2016/11/8.
 */

public class BaseActivity extends AppCompatActivity {
    private static ArrayList<AppCompatActivity> arrayList =new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrayList.remove(this);
    }

    /**
     * 销毁所有活动
     */
    public static void  finishAll(){
        for(AppCompatActivity aca:arrayList){
            aca.finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获得输入法的服务
        Log.i("Current View id",getCurrentFocus().getId()+"");
        if(getCurrentFocus()!=null){
            InputMethodManager inputMethod= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           return inputMethod.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

        return super.onTouchEvent(event);    }
}
