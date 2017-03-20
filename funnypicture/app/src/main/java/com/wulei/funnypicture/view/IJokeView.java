package com.wulei.funnypicture.view;

import android.view.View;

import com.wulei.funnypicture.model.JokeGson;

import java.util.List;

/**
 * Created by wulei on 2017/2/9.
 */

public interface IJokeView<T> {
    /**
     * 显示progressbar
     */
    void showProgress();
    /**
     * 隐藏progressbar
     */
    void hideProgress();
    /**
     * 找到item的数据
     */
    void setItemData(List<T> list);

    /**
     * 获取view对象
     * @return
     */
    View getSnackView();

}
