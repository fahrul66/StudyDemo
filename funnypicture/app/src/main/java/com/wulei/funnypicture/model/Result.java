package com.wulei.funnypicture.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by wulei on 2017/2/9.
 */

public class Result<T> {
    @SerializedName("data")
    @Expose
    private List<T> data ;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
