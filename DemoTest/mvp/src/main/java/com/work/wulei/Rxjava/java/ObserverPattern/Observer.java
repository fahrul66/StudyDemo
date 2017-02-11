package com.work.wulei.Rxjava.java.ObserverPattern;

/**
 * Created by wulei on 2016/12/31.
 */

public interface Observer {
    String getName();

    void setName(String name);

    void help();

    void beAttacked(AllControlCenter acc);
}
