package com.work.wulei.Rxjava.java.ObserverPattern;

/**
 * Created by wulei on 2016/12/31.
 */

public class ConcreteAllyControlCenter extends AllControlCenter {

    public ConcreteAllyControlCenter(Float name) {
        System.out.println(name+"战队组建成功！");
        this.guPiao = name;
    }

    @Override
    void notifyOtherPlayer(String name) {
         System.out.println("股票的变化幅度为："+this.guPiao+"%");
        for (Player player : list) {
//            if (!(player.getName().equalsIgnoreCase(name))){
//
//                player.help();
//            }
            if (guPiao<5.0f){

            }
        }
    }
}
