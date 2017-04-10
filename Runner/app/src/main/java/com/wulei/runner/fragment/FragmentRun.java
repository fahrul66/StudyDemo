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
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.wulei.runner.R;
import com.wulei.runner.app.App;
import com.wulei.runner.customView.ArcProgressBar;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.model.LocalSqlPedometer;
import com.wulei.runner.service.StepInPedometer;
import com.wulei.runner.service.StepService;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DateUtils;
import com.wulei.runner.utils.DialogUtils;
import com.wulei.runner.utils.FragmentUtils;
import com.wulei.runner.utils.PermissionUtil;
import com.wulei.runner.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wulei on 2017/3/27.
 */

public class FragmentRun extends BaseFragment implements View.OnClickListener {

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

    //昨天的步数
    private int yesSteps;


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
        /*
         * 程序销毁的时候，进行数据的保存，如 calorie，goals，等待
         * 在 oncreate的时候进行，数据的填充。
         */
        super.onDestroy();
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
    }

    /**
     * 数据初始化
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        Intent intent = new Intent(mActivity, StepService.class);
        mActivity.startService(intent);

    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantFactory.INIT:
                    int step = (int) msg.getData().get("steps");
                    if (mArc != null) {

                        mArc.setProgress(step);
                    }
                    break;
                case ConstantFactory.SAVE:
                    int steps = (int) msg.getData().get("steps");
                    mArc.setProgress(steps);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 判断是今天还是昨天
     *
     * @param list
     * @param today
     * @return
     */
    private boolean isToday(List<LocalSqlPedometer> list, String today) {
        for (LocalSqlPedometer pedometer : list) {
            String date = pedometer.getDate();
            if (date.equals(today)) {
                //数据填充
                return true;
            }
        }
        return false;
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


}
