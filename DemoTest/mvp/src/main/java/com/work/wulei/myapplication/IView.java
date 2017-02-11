package com.work.wulei.myapplication;

/**
 * Created by wulei on 2016/12/28.
 */

import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * view的接口类，用于规定view的行为
 */
public interface IView  {
    /**
     * 登陆显示，隐藏进度
     */
    void showProgress();
    void hideProgress();


    /*******************************/

    /**
     * button的登陆清除。
     */

    String getUser();
    String getPWD();
    void clear();

    void showToast(String msg);
}
