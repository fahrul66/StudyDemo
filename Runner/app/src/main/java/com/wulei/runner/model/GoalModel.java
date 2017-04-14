package com.wulei.runner.model;

/**
 * Created by wule on 2017/04/14.
 * 实体类，创建fragmentGoal
 */

public class GoalModel {
    private String goal;
    private String infact;
    private boolean isComplete;
    private String date;

    public GoalModel(String date, String goal, String infact, boolean isComplete) {
        this.date = date;
        this.goal = goal;
        this.infact = infact;
        this.isComplete = isComplete;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getInfact() {
        return infact;
    }

    public void setInfact(String infact) {
        this.infact = infact;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
