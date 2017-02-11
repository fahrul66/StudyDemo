package com.wulei.funnypicture.presenter;

import android.support.design.widget.Snackbar;

import com.wulei.funnypicture.model.JokeGson;
import com.wulei.funnypicture.view.IJokeView;

import java.util.List;

/**
 * Created by wulei on 2017/2/10.
 */

public class JokePresenter implements IJokePresenter,IfindItemPresenter.FinishedListener<JokeGson>,IfindItemPresenter.ErrorListener<JokeGson> {
    /**
     * view的引用
     */
    private IJokeView iJokeView;
    private IfindItemPresenter<JokeGson> ifindItemPresenter;

    public JokePresenter(IJokeView iJokeView) {
        this.iJokeView = iJokeView;
        ifindItemPresenter = new JokeFindItemPresenter();
    }

    @Override
    public void onResume() {
        if (iJokeView!=null){
            iJokeView.showProgress();
            //刷新操作....
            ifindItemPresenter.getItemDataRefresh(this,this);
        }
    }

    /**
     * 避免内存泄漏，释放资源
     */
    @Override
    public void onDestory() {
         if (iJokeView!=null){
             iJokeView = null;
         }
    }

    @Override
    public void getRefreshData() {
        ifindItemPresenter.getItemDataRefresh(this,this);
    }

    @Override
    public void getPullData() {
        ifindItemPresenter.getItemDataPull(this,this);
    }



    /**
     * 数据加载完成
     */
    @Override
    public void finished(List<JokeGson> list) {
        if (iJokeView != null) {
            iJokeView.hideProgress();
            iJokeView.setItemData(list);
        }
    }

    /**
     * 数据加载失败
     */
    @Override
    public void error() {
        if (iJokeView != null) {
            iJokeView.hideProgress();
            Snackbar.make(iJokeView.getSnackView(), "数据加载出错", Snackbar.LENGTH_SHORT).show();
        }
    }
}
