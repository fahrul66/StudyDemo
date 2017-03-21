package com.wulei.runner.utils;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.wulei.runner.app.App;

public class ToastUtil {

    private ToastUtil() {
    }

    public static void show(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.length() < 10) {
                Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void show(@StringRes int resId) {
        show(App.getInstance().getResources().getString(resId));
    }

}