package com.wulei.funnypicture.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wulei on 2017/2/9.
 */

public class PicsGson {
    @SerializedName("content") //序列化的名字，toJson序列化，fromJson反序列化,避免命名不同而重写代码
    @Expose  //在使用GsonBulider的时候，可以序列化和反序列化，默认为true，没有此标记则不能。
    private String content;
    @SerializedName("hashId")
    @Expose
    private String hashId;
    @SerializedName("unixtime")
    @Expose
    private int unixtime;
    @SerializedName("updatetime")
    @Expose
    private String updatetime;
    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public int getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(int unixtime) {
        this.unixtime = unixtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

}
