package com.wulei.runner.model;

/**
 * Created by wule on 2017/04/07.
 */

public class LocalSqlPedometer {
    //步数
    private int steps;
    //运动公里数
    private double km;
    //运动消耗卡路里
    private double calorie;
    //运动的目标
    private int goals;
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

    public void setCalorie(int calorie) {
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

    public void setKm(int km) {
        this.km = km;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
