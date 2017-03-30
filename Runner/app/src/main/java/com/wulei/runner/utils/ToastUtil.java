package com.wulei.runner.utils;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.wulei.runner.app.App;

public class ToastUtil {
    /**
     * toast实例
     */
    static Toast mToast;

    private ToastUtil() {
    }

    /**
     * Toast的单例模式
     *
     * @param text toast显示的文本
     */
    public static void show(CharSequence text) {
        if (mToast != null) {
            //更新text,显示
            mToast.setText(text);
            mToast.show();
        } else {
            //重新创建显示
            if (text.length() < 10) {
                mToast = Toast.makeText(App.mAPPContext, text, Toast.LENGTH_SHORT);
            } else {
                mToast = Toast.makeText(App.mAPPContext, text, Toast.LENGTH_LONG);
            }
            //显示
            mToast.show();
        }
    }

    /**
     * Toast的单例模式
     *
     * @param resId strings.xml中的id
     */
    public static void show(@StringRes int resId) {
        show(App.mAPPContext.getString(resId));
    }
}