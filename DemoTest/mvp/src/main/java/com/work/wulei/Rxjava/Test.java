package com.work.wulei.Rxjava;

/**
 * Created by wulei on 2017/1/7.
 */

public class Test {
    public static void main(String[] args) {

        /**
         * 创造灯对象
         */
        Light light1 = new Light("灯1");
        Light light2 = new Light("灯2");
        Light light3 = new Light("灯3");
        Light light4 = new Light("灯4");
        Light light5 = new Light("灯5");

        /**
         * 被观察者Observable
         */
        Switch swith = new Switch();

        swith.add(light1);
        swith.add(light2);
        swith.add(light3);
        swith.add(light4);
        swith.add(light5);

        //关灯
        swith.closeLight();

    }
}
