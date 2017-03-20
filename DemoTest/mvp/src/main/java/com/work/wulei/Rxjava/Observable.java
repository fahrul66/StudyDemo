package com.work.wulei.Rxjava;

/**
 * Created by wulei on 2017/1/7.
 */

public interface Observable {
    void add(Observer observer);
    void remove(Observer observer);
    void closeLight();
}
