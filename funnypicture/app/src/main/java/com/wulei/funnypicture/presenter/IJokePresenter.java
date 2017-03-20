package com.wulei.funnypicture.presenter;

/**
 * Created by wulei on 2017/2/10.
 */

public interface IJokePresenter {
    /**
     * 业务逻辑，刷新，销毁，点击，获取数据
     */
    void onResume();
    void onDestory();
    void getRefreshData();
    void getPullData();
}
