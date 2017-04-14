package com.wulei.runner.utils;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by wule on 2017/04/11.
 */

public class SnackbarUtil {

    public static void show(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(View view, @StringRes int strID) {
        Snackbar.make(view, strID, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(View view, String msg, String btnMsg, View.OnClickListener action) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setAction(btnMsg,action)
                .show();
    }

    public static void show(View view, @StringRes int strID, @StringRes int btnMsg, View.OnClickListener action) {
        Snackbar.make(view, strID, Snackbar.LENGTH_SHORT)
                .setAction(btnMsg,action)
                .show();
    }
}
