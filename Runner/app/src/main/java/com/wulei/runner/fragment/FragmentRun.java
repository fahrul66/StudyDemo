package com.wulei.runner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.wulei.runner.app.App;
import com.wulei.runner.customView.ArcProgressBar;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.model.LocalSqlModel;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DialogUtils;
import com.wulei.runner.utils.FragmentUtils;
import com.wulei.runner.utils.PermissionUtil;
import com.wulei.runner.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wulei on 2017/3/27.
 */

public class FragmentRun extends BaseFragment implements View.OnClickListener, SensorEventListener {

    @BindView(R.id.btn_start_run)
    Button mButton;
    @BindView(R.id.arcProgressbar_run)
    ArcProgressBar mArc;
    /*
     *传感器设置
     */
    private SensorManager mSensorManger;
    private Sensor mStepSensor;
    /*
     * 数据库设置
     */
    private LocalSqlHelper lsh;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * 销毁，注销传感器
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManger.unregisterListener(this);
        lsh.close();
    }

    /**
     * 布局优化
     *
     * @return
     */
    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_run;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * 传感器设置
         */
        mSensorManger = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        mStepSensor = mSensorManger.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorManger.registerListener(this, mStepSensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 数据初始化
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        /*
         * 初始化arcProgressBar的数值。
         * 1.未登录，本地数据库读取
         * 2.已登录，云数据库读取
         */
        lsh = new LocalSqlHelper(App.mAPPContext);
        List<LocalSqlModel> list = lsh.query();
        //数据为空
        if (!list.isEmpty()) {
//            mArc.setProgress(list.get(0).getSteps());
        }

    }

    /**
     * 监听器设置
     */
    @Override
    protected void setListener() {
        mButton.setOnClickListener(this);
    }

    /**
     * 逻辑处理
     *
     * @param savedInstanceState
     */
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
                mButton.setVisibility(View.GONE);
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
    public void onSensorChanged(SensorEvent event) {
        int steps = (int) event.values[0];
        //更新数据
        mArc.setProgress(steps);
        //写入本地数据库
        lsh.insert(new LocalSqlModel(steps, null, null, null, null));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
