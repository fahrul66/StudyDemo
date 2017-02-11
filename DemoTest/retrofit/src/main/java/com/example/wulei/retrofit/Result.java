package com.example.wulei.retrofit;

/**
 * Created by wulei on 2017/1/8.
 */
//-----------------------------------com.example.Result.java-----------------------------------


import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("data")
    @Expose
    private List<Datum> data ;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}