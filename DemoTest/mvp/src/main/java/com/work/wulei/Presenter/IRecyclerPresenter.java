package com.work.wulei.Presenter;

/**
 * Created by wulei on 2016/12/29.
 */

public interface IRecyclerPresenter {
    //业务逻辑

    void onResume();
    void onDestory();
    void onItemClick(int position);
}
