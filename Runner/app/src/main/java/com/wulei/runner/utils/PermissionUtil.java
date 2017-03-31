package com.wulei.runner.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

/**
 * Created by wule on 2017/03/31.
 */

public class PermissionUtil extends Activity {

    private static final int REQUEST_CODE_LOCATION = 1;

    /**
     * android 6.0运行时权限
     */
    private void runtimePermisson() {
        //如果当前的API版本大于等于23，即android6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查自身是否有定位权限
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission_group.STORAGE}, REQUEST_CODE_LOCATION);
            }
        }
    }

    /**
     * 选择是否打开权限后，会调用这里的方法，处理接收或者不接受权限的响应
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                //做相应的处理
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(getWindow().getDecorView(), "权限处理成功！", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getWindow().getDecorView(), "权限处理失败！", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
