package com.wulei.runner.model;

/**
 * Created by wule on 2017/04/10.
 */

public class LocalSqlRun {
    //跑步的数据 time text,km REAL,speed REAL,picUrl text,date text,address text
    //跑步的时间，转换00:00格式
    private String time;
    //跑步的公里数
    private double km;
    //跑步的速度，gps
    private double speed;
    //跑步的截图
    private String picUrl;
    //跑步的日期
    private String date;
    //跑步的定位，地址
    private String address;

    public LocalSqlRun() {
    }

    public LocalSqlRun(String time, double km, double speed, String picUrl, String date, String address) {
        this.time = time;
        this.km = km;
        this.speed = speed;
        this.picUrl = picUrl;
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

    public void setKm(double km) {
        this.km = km;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
