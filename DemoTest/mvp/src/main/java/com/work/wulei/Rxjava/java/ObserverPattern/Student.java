package com.work.wulei.Rxjava.java.ObserverPattern;

import java.util.List;

/**
 * Created by wulei on 2017/1/3.
 */

public class Student {
    private String mName;
    private List<Course> list;

    public Student(String mName, List<Course> list) {
        this.mName = mName;
        this.list = list;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    /**
     * 创建数组
     */
    public List<Course> getCourseInstance() {
        return list;
    }
}
