package com.work.wulei.model;

import java.util.List;

/**
 * Created by wulei on 2016/12/29.
 */

public interface IRecyclerFindItem {

    void findItem(onFinishedListener finishedListener);

    //回调
    interface onFinishedListener{
        void finished(List<String> list);
    }
}
