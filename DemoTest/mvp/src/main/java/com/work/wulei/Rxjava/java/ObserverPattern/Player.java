package com.work.wulei.Rxjava.java.ObserverPattern;

/**
 * Created by wulei on 2016/12/31.
 */

public class Player implements Observer {

    public Player(String name) {
        this.name = name;
    }

    private String name;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
         this.name = name;
    }

    @Override
    public void help() {
       System.out.println(name+"请求帮助！");
    }

    @Override
    public void beAttacked(AllControlCenter acc) {
        System.out.println(name+"被攻击！");

        acc.notifyOtherPlayer(name);
    }
}
