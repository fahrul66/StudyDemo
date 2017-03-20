package com.work.wulei.Presenter;

/**
 * Created by wulei on 2016/12/28.
 */

public interface IPresenter {

    /**
     * 业务逻辑，如登陆，清除
     */
    void login();
    void clear();
    void onDestory();
}
