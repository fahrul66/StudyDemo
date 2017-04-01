package com.wulei.runner.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

/**
 * Created by wule on 2017/04/01.
 */

public class DialogUtils {

    /**
     * 显示普通的dialog弹出框
     * @param context
     * @param title
     * @param msg
     * @param onClickListener
     */
    public static void showAlert(Context context, String title, String msg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(title)
                .setMessage(msg)
                .setNegativeButton("取消",null)
                .setPositiveButton("确定",onClickListener)
                .setCancelable(false)
                .show();
    }

    /**
     * 资源id，dialog弹出框
     * @param context
     * @param title
     * @param msg
     * @param onClickListener
     */
    public static void showAlert(Context context, @StringRes int title, @StringRes int msg, DialogInterface.OnClickListener onClickListener) {
        showAlert(context,context.getResources().getString(title),context.getResources().getString(msg),onClickListener);
    }
}