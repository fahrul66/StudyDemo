package com.wulei.runner.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by base on 2016/8/17.
 */
public class StepInPedometer implements SensorEventListener {
    private int liveStep = 0;
    public static int CURRENT_SETP = 0;
    private Context context;
    private Sensor countSensor;
    private SensorManager sensorManager;


    public StepInPedometer(Context context) {
        this.context = context;
        //初始化
        addCountStepListener();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        liveStep = (int) event.values[0];
        CURRENT_SETP = liveStep;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 传感器创建，监听
     */
    private void addCountStepListener() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //是否为空
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 销毁监听
     */
    public void destory() {
        sensorManager.unregisterListener(this);
    }
}
