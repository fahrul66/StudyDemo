package com.work.wulei.Rxjava.java.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wulei on 2016/12/31.
 */

public abstract class AllControlCenter {

      protected Float guPiao;
      protected List<Player> list = new ArrayList<>();

    public Float getAllyName() {
        return guPiao;
    }

    public void setAllyName(Float allyName) {
        this.guPiao = allyName;
    }

    public void join(Player player){
        System.out.println(player.getName()+"购买了股票！");
        list.add(player);
    }
    public void quit(Player player){
        System.out.println(player.getName()+"抛出了此股票！");
        list.remove(player);
    }

    abstract void notifyOtherPlayer(String name);
}
