package com.wulei.runner.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wule on 2017/04/20.
 */

public class User extends BmobUser {
    //跑步的数据 time text,km REAL,speed REAL,picUrl text,date text,address text
    //跑步的时间，转换00:00格式
    private String time;
    //跑步的公里数
    private Double km;
    //跑步的速度，gps
    private Double speed;
    //跑步的截图
    private BmobFile pic;
    //跑步的日期
    private String date;
    //跑步的定位，地址
    private String address;

    public User() {
    }

    public User(String time, double km, double speed, BmobFile pic, String date, String address) {
        this.time = time;
        this.km = km;
        this.speed = speed;
        this.pic = pic;
        this.date = date;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public BmobFile getPicUrl() {
        return pic;
    }

    public void setPicUrl(BmobFile picUrl) {
        this.pic = pic;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
