package com.work.wulei.myapplication;

import java.util.List;

/**
 * Created by wulei on 2016/12/29.
 */

public interface IRecyclerView {

    //progress的view
    void showProgress();
    void hideProgress();

    //recyclerView需要有item，设置数据，传递给view让他更新
    void setItemData(List<String> list);
    //点击后
    void showItemMsg(String msg);
}
