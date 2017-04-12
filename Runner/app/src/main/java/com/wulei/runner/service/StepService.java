package com.wulei.runner.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.wulei.runner.R;
import com.wulei.runner.activity.MainActivity;
import com.wulei.runner.app.App;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.fragment.FragmentRun;
import com.wulei.runner.model.LocalSqlPedometer;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DateUtils;
import com.wulei.runner.utils.FragmentUtils;

import java.util.List;

public class StepService extends Service {
    //默认为30秒进行一次存储
    private static int duration = 30000;
    private NotificationManager nm;
    private NotificationCompat.Builder builder;
    //广播接收器
    private BroadcastReceiver mBatInfoReceiver;
    private TimeCount time;
    //当天的日期
    private String CURRENTDATE = "";
    //数据库对象
    LocalSqlHelper localSqlHelper = new LocalSqlHelper(App.mAPPContext);

    //关机后的数据
    private static int shutDown = 0;
    private boolean flagS = false;
    //日期变化
    private static int dateChange = 0;
    private boolean flagD = false;
    //开始数据，传感器累计值
    private int startValue = 0;
    private int finalVaule = 0;
    private int today;
    private StepInPedometer mode;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化广播接收器

        initBroadcastReceiver();
        startStep();
        startTimeCount();
    }

    /**
     * 初始化广播
     */
    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        //日期修改
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                //finalValue
                finalVaule = StepInPedometer.CURRENT_SETP + shutDown - dateChange - startValue;
                String action = intent.getAction();

                if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {
                    /*
                     * 日期改变后，数据清零，但是计算的要是差值
                     */
                    flagD = true;
                    initTodayData();
                    clearStepData();
                    //更新0
                    updateNotification("今日步数：" + finalVaule + " 步");
                } else if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                    /*
                     *关机后的做法，数据为空，之前的数据加上，后来的数据。
                     */
                    flagS = true;
                    initTodayData();
                    clearStepData();
                    //更新0
                    updateNotification("今日步数：" + finalVaule + " 步");
                }
            }
        };
        //注册广播
        registerReceiver(mBatInfoReceiver, filter);
    }

    /**
     *
     */
    private void startStep() {
        //初始化传感器
        mode = new StepInPedometer(this);
        //获取当天类
        CURRENTDATE = DateUtils.convertToStr(System.currentTimeMillis());
    }

    /**
     * 开始倒计时，30秒，保存一次
     */
    private void startTimeCount() {
        time = new TimeCount(duration, 1000);
        time.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initTodayData();
        updateNotification("今日步数：" + today + " 步");
        startTimeCount();
        return START_STICKY;
    }

    /**
     * 初始化数据
     */
    private void initTodayData() {

        //获取当天时间
        CURRENTDATE = DateUtils.convertToStr(System.currentTimeMillis());
        //获取当天的数据，用于展示,有没有当天数据
        List<LocalSqlPedometer> list = localSqlHelper.queryJB("date", CURRENTDATE, null);
        //无数据。
        if (list.size() == 0 || list.isEmpty()) {
            //默认的内置传感器步数,累计步数
            if (StepInPedometer.CURRENT_SETP != 0) {
                //复制给初始值
                startValue = StepInPedometer.CURRENT_SETP;
            }
            //初始化为0
            StepInPedometer.CURRENT_SETP = 0;
            today = 0;

        } else if (list.size() == 1) {
            StepInPedometer.CURRENT_SETP = list.get(0).getSteps();
            today = list.get(0).getSteps();
        }


        /**
         *  从数据库中读取，前一天的数据,默认是按id 倒叙排列
         */
        List<LocalSqlPedometer> lastDay = localSqlHelper.queryJB(null);
        //关机后的数据，记录一次，每次广播
        if (flagS) {

            shutDown = lastDay.get(0).getSteps();
        }
        //日期改变后的数据，记录一次，每次广播
        if (flagD) {
            dateChange = lastDay.get(0).getSteps();
        }
        //重置
        flagS = false;
        flagD = false;

        //数据传递
        handler(today + shutDown - dateChange - startValue, ConstantFactory.INIT);
    }


    /**
     * update notification
     */
    private void updateNotification(String content) {
        //bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.circle);
        //intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        builder = new NotificationCompat.Builder(this);
        builder.setPriority(Notification.PRIORITY_MIN)
                .setContentIntent(contentIntent)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.circle)
                .setTicker(getResources().getString(R.string.app_name))
                .setContentTitle(getResources().getString(R.string.app_name))
                //设置不可清除
                .setOngoing(true)
                .setContentText(content);
        Notification notification = builder.build();
        //开启前台服务
        startForeground(0, notification);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //显示通知
        nm.notify(R.string.app_name, notification);
    }

    /**
     * 倒计时，用来30s中保存一次数据
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    /**
     * 数据保存到数据库
     */
    private void save() {
        //数据传递,日期改变，减去差值。关机清除数据，加上前值。
        int tempStep = StepInPedometer.CURRENT_SETP + shutDown - dateChange - startValue;
        //更新notification
        updateNotification("今日步数：" + tempStep + " 步");
        //handler
        handler(tempStep, ConstantFactory.SAVE);
        //数据保存
        List<LocalSqlPedometer> list = localSqlHelper.queryJB("date", CURRENTDATE, null);
        //无今日数据
        if (list.size() == 0 || list.isEmpty()) {
            LocalSqlPedometer lsp = new LocalSqlPedometer();
            lsp.setDate(CURRENTDATE);
            lsp.setSteps(tempStep);
            localSqlHelper.insert(lsp);


        } else if (list.size() == 1) {
            LocalSqlPedometer data = list.get(0);
            data.setSteps(tempStep);
            localSqlHelper.update(ConstantFactory.SQL_TABLE_JB, "steps", tempStep, "date", CURRENTDATE);
        }


    }

    /**
     * 数据传递
     *
     * @param tempStep
     */
    private void handler(int tempStep, int what) {
        //handler
        Message m = new Message();
        m.what = what;
        Bundle bn = new Bundle();
        bn.putInt("steps", tempStep);
        m.setData(bn);
        ((FragmentRun) FragmentUtils.newInstance(ConstantFactory.TAG_RUN)).handler.sendMessage(m);
    }

    /**
     * 清除步数
     */
    private void clearStepData() {
        StepInPedometer.CURRENT_SETP = 0;
    }

    @Override
    public void onDestroy() {
        //取消前台进程
        stopForeground(true);
        localSqlHelper.close();
        unregisterReceiver(mBatInfoReceiver);
        mode.destory();
        Intent intent = new Intent(this, StepService.class);
        startService(intent);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
