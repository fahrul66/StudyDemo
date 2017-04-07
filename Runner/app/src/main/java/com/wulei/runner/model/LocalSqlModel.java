package com.wulei.runner.model;

/**
 * Created by wule on 2017/04/07.
 */

public class LocalSqlModel {
    //步数
    private int steps;
    //运动时间
    private String time;
    //运动公里数
    private String km;
    //运动的目标
    private String goals;
    //运动的截图，本地保存url
    private String picUrl;

    public LocalSqlModel() {
    }

    public LocalSqlModel(int steps, String time, String km, String goals, String picUrl) {
        this.steps = steps;
        this.time = time;
        this.km = km;
        this.goals = goals;
        this.picUrl = picUrl;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
