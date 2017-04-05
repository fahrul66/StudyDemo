package com.wulei.runner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;

import com.wulei.runner.R;
import com.wulei.runner.activity.MainActivity;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DialogUtils;
import com.wulei.runner.utils.FragmentUtils;
import com.wulei.runner.utils.PermissionUtil;
import com.wulei.runner.utils.ToastUtil;

import butterknife.BindView;

/**
 * Created by wulei on 2017/3/27.
 */

public class FragmentRun extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btn_start_run)
    Button mButton;
    @BindView(R.id.fab_run)
    FloatingActionButton mFab;
    /*
     * 按钮的变化标识
     */
    boolean btnFlag = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_run;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {
        mButton.setOnClickListener(this);
        mFab.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_run:
                //权限和gps判断
                startRun();
                //按钮变化判断
                if (btnFlag) {
                    mButton.setVisibility(View.GONE);
                    mFab.setVisibility(View.VISIBLE);

//                    int x
//                    ViewAnimationUtils.createCircularReveal(mButton,)
                } else {
                    mButton.setVisibility(View.VISIBLE);
                    mFab.setVisibility(View.GONE);

                }
                break;
            case R.id.fab_run:
                btnFlag = false;
                mButton.setVisibility(View.VISIBLE);
                mFab.setVisibility(View.GONE);
                break;
        }

    }

    /**
     * 开始运动，准备工作
     */
    private void startRun() {
        //6.0权限问题
        PermissionUtil.getInstance().runtimePermisson(this, ConstantFactory.PERMISSION, ConstantFactory.REQUEST_CODE, new PermissionUtil.OnPermissionResultListener() {
            @Override
            public void permissionGranted() {
                //授权成功,进行跳转
                //判断gps，是否打开。
                if (isGPSOpen()) {
                    //只有成功跳转的时候才会使按钮变形。
                    btnFlag = true;

                    //切换fragment
                    FragmentUtils.hide(mActivity, FragmentUtils.newInstance(ConstantFactory.TAG_RUN));
                    FragmentUtils.add(mActivity, FragmentUtils.newInstance(ConstantFactory.TAG_MAP), ConstantFactory.TAG_MAP);
                    FragmentUtils.show(mActivity, FragmentUtils.newInstance(ConstantFactory.TAG_MAP));

                } else {
                    //跳转到gps
                    toGpsSetting();
                }
            }

            @Override
            public void permissionDenied() {
                //授权失败，跳转到权限页面
                DialogUtils.showAlert(mActivity, R.string.dialog_permission_title, R.string.dialog_permission_msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //页面跳转，permission
                        toPermissionSetting();
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
        String packageName = mActivity.getPackageName();
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * 跳转到GPs界面
     */
    private void toGpsSetting() {
        ToastUtil.show(R.string.toast_gps);
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    /**
     * 判断gps是否打开
     *
     * @return
     */
    private boolean isGPSOpen() {
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }


    /**
     * 回调，permission
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
    }
}
