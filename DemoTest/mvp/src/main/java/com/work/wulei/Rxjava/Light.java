package com.work.wulei.Rxjava;

/**
 * Created by wulei on 2017/1/7.
 */

public class Light implements Observer {
    private String name;

    public Light(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void close() {

//         observable.closeLight();
        System.out.println(name+"关闭了！");
    }
}
