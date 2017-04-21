package com.wulei.runner.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DialogUtils;
import com.wulei.runner.utils.PermissionUtil;

/**
 * Created by wule on 2017/03/21.
 * 是程序的启动画面
 */

public class StartUpActivity extends BaseActivity {


    @Override
    protected void hideShowFragment() {

    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_startup;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //permission
//6.0权限问题
        PermissionUtil.getInstance().runtimePermisson(this, ConstantFactory.READ_PHONE_STATE, ConstantFactory.REQUEST_CODE, new PermissionUtil.OnPermissionResultListener() {
            @Override
            public void permissionGranted() {
                //授权成功,进行跳转

                //延迟跳转
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(new Intent(StartUpActivity.this, MainActivity.class));
                    }
                }, 2000);

            }

            @Override
            public void permissionDenied() {
                //授权失败，跳转到权限页面
                DialogUtils.showAlert(StartUpActivity.this, R.string.dialog_permission_title, R.string.dialog_permission_msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //页面跳转，permission
                        toPermissionSetting();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //销毁
                        finish();
                    }
                });


            }
        });

    }

    /**
     * 跳转到权限界面
     */
    private void toPermissionSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        String packageName = getPackageName();
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
