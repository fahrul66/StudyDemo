package com.wulei.runner.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by wule on 2017/04/07.
 */

public class LocalSqlPedometer extends BmobObject{
    //步数
    private Integer steps;
    //运动公里数
    private Double km;
    //运动消耗卡路里
    private Double calorie;
    //运动的目标
    private Integer goals;
    //计步的日期
    private String date;

    public LocalSqlPedometer() {
    }

    public LocalSqlPedometer(int steps, double km, double calorie, int goals, String date) {
        this.steps = steps;
        this.km = km;
        this.calorie = calorie;
        this.goals = goals;
        this.date = date;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
