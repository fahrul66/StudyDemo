package com.wulei.funnypicture.presenter;

import android.support.design.widget.Snackbar;

import com.wulei.funnypicture.model.PicsGson;
import com.wulei.funnypicture.view.IJokeView;

import java.util.List;

/**
 * Created by wulei on 2017/2/10.
 */

public class PicsPresenter implements IJokePresenter, IfindItemPresenter.FinishedListener<PicsGson>, IfindItemPresenter.ErrorListener<PicsGson> {
    /**
     * view的引用
     */
    private IJokeView iJokeView;
    private IfindItemPresenter<PicsGson> ifindItemPresenter;

    public PicsPresenter(IJokeView iJokeView) {
        this.iJokeView = iJokeView;
        ifindItemPresenter = new PicsFindItemPresenter();
    }

    @Override
    public void onResume() {
        if (iJokeView != null) {
            iJokeView.showProgress();
            //刷新操作....
            ifindItemPresenter.getItemDataRefresh(this, this);
        }
    }

    /**
     * 避免内存泄漏，释放资源
     */
    @Override
    public void onDestory() {
        if (iJokeView != null) {
            iJokeView = null;
        }
    }

    @Override
    public void getRefreshData() {
        ifindItemPresenter.getItemDataRefresh(this, this);
    }

    @Override
    public void getPullData() {
        ifindItemPresenter.getItemDataPull(this, this);
    }


    /**
     * 数据加载完成
     */
    @Override
    public void finished(List<PicsGson> list) {
        if (iJokeView != null) {
            iJokeView.hideProgress();
            iJokeView.setItemData(list);
        }
    }

    /**
     * 数据出错
     */
    @Override
    public void error() {
        if (iJokeView != null) {
            iJokeView.hideProgress();
            Snackbar.make(iJokeView.getSnackView(), "数据加载出错", Snackbar.LENGTH_SHORT).show();
        }
    }
}
