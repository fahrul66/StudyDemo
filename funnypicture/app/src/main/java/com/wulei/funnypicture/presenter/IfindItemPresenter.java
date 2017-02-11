package com.wulei.funnypicture.presenter;

import android.view.View;

import java.util.List;

/**
 * Created by wulei on 2017/2/10.
 */

public interface IfindItemPresenter<T> {
    /**
     * 获取数据
     */
    void getItemDataRefresh(FinishedListener<T> finishedListener,ErrorListener<T> errorListener);

    void getItemDataPull(FinishedListener<T> finishedListener,ErrorListener<T> errorListener);

    /**
     * 数据加载完成，回调接口
     */
    interface FinishedListener<T>{
        void finished(List<T> list);
    }
    /**
     * 数据出错，回调接口
     */
    interface ErrorListener<T>{
        void error();
    }
}
