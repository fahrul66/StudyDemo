package com.wulei.runner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wulei.runner.R;
import com.wulei.runner.activity.BDMapActivity;
import com.wulei.runner.app.App;
import com.wulei.runner.customView.ArcProgressBar;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.model.LocalSqlPedometer;
import com.wulei.runner.service.StepService;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DateUtils;
import com.wulei.runner.utils.DialogUtils;
import com.wulei.runner.utils.PermissionUtil;
import com.wulei.runner.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wulei on 2017/3/27.
 */

public class FragmentRun extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btn_start_run)
    Button mButton;
    @BindView(R.id.arcProgressbar_run)
    public ArcProgressBar mArc;
    @BindView(R.id.tv_km_data_run)
    TextView mKm;
    @BindView(R.id.tv_calorie_data_run)
    TextView mCalorie;
    @BindView(R.id.tv_maxStep_data_run)
    TextView mMaxStep;
    /*
     * 数据库设置
     */
    private LocalSqlHelper lsh;
    //默认的每日目标 5000步
    public static int goals = 5000;

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

    /**
     * 数据初始化
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        //初始化数据库
        lsh = new LocalSqlHelper(App.mAPPContext);
        //开启服务
        Intent intent = new Intent(mActivity, StepService.class);
        mActivity.startService(intent);

        //初始化arc
        mArc.setMaxProgress(goals);
        mArc.setBottomText("目标：" + goals);
        //读取数据库的数据，maxStep
        List<LocalSqlPedometer> list = lsh.queryJB("steps desc");
        if (!list.isEmpty()) {
            //最大的步数
            int maxStep = list.get(0).getSteps();
            mMaxStep.setText(String.valueOf(maxStep));
        } else {
            mMaxStep.setText("0.0");
        }

    }

    /**
     * 根据步数，计算km ,calorie
     *
     * @param steps
     */
    private void initText(int steps) {
        //设置步数
        double km = (steps * 0.5) / 1000;

        mKm.setText(String.valueOf(keepNum(km)));
        //消耗的卡路里
        double car = steps * 0.04;
        mCalorie.setText(String.valueOf(keepNum(car)));
        //最大的步数，一天更新一次。
    }

    /**
     * 保留小数
     */
    private double keepNum(double v) {
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
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
        String today = DateUtils.convertToStr(System.currentTimeMillis());
        double calorie = Double.parseDouble(mCalorie.getText().toString());
        double km = Double.parseDouble(mKm.getText().toString());
        //数据保存
        lsh.update(ConstantFactory.SQL_TABLE_JB, "calorie", calorie, "date", today);
        lsh.update(ConstantFactory.SQL_TABLE_JB, "km", km, "date", today);
        lsh.update(ConstantFactory.SQL_TABLE_JB, "goals", goals, "date", today);
        super.onDestroy();
    }

    /**
     * 从服务端获取数据
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantFactory.INIT:
                    int step = (int) msg.getData().get("steps");
                    if (mArc != null && step >= 0) {
                        //更新mrc数据
                        mArc.setProgress(step);
                        //更新数据
                        initText(step);

                    }
                    break;
                case ConstantFactory.SAVE:
                    int steps = (int) msg.getData().get("steps");
                    if (mArc != null && steps >= 0) {
                        mArc.setProgress(steps);
                        //更新数据
                        initText(steps);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


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
//                    FragmentUtils.hide(mActivity, FragmentUtils.newInstance(ConstantFactory.TAG_RUN));
//                    FragmentUtils.add(mActivity, FragmentUtils.newInstance(ConstantFactory.TAG_MAP), ConstantFactory.TAG_MAP);
//                    FragmentUtils.show(mActivity, FragmentUtils.newInstance(ConstantFactory.TAG_MAP));
                    startActivity(new Intent(mActivity, BDMapActivity.class));

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