package com.work.wulei.Rxjava;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wulei on 2017/1/7.
 */

public class Switch implements Observable {
    /**
     * 维护一个，观察者灯的集合
     */
    List<Observer> list = new ArrayList<>();

    @Override
    public void closeLight() {

        for (Observer light : list) {
              //关灯
              light.close();
        }
    }

    @Override
    public void remove(Observer observer) {
        list.remove(observer);
    }

    @Override
    public void add(Observer observer) {
        list.add(observer);
    }
}
