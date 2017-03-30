package com.wulei.runner.borcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wulei.runner.R;
import com.wulei.runner.utils.ToastUtil;

/**
 * Created by wule on 2017/03/30.
 */

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isNetworkAvaliable(context)) {
            ToastUtil.show(context.getResources().getString(R.string.network_failure));
        }
    }

    /**
     * 判断网络是否可用
     */
    private boolean isNetworkAvaliable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //判断是否可用
        return netInfo != null && netInfo.isAvailable();
    }
}

