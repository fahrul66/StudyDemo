package com.work.wulei.Presenter;

import com.work.wulei.model.IRecyclerFindItem;
import com.work.wulei.model.RecyclerFindItemImpl;
import com.work.wulei.myapplication.IRecyclerView;
import com.work.wulei.myapplication.IView;

import java.util.List;

/**
 * Created by wulei on 2016/12/29.
 */

public class RecyclerPresenter implements IRecyclerPresenter , RecyclerFindItemImpl.onFinishedListener{

    private IRecyclerView mIView;
    private IRecyclerFindItem mIRecyclerFindItem;

    public RecyclerPresenter(IRecyclerView mIView){

        //mvp中view不能和model直接访问，所以必须在presenter中传递，实例化
        this.mIRecyclerFindItem = new RecyclerFindItemImpl();
        this.mIView = mIView;
    }

    @Override
    public void onResume() {
        if (mIView !=null) {
            mIView.showProgress();
        }
        mIRecyclerFindItem.findItem(this);
    }

    /**
     * 为甚么每次都要写onDestory？
     * 因为，Presenter中持有activity的引用（他实现了view的接口），如果activity被销毁，而Presenter还继续持有
     * activity的引用，导致activity不能被回收，内存泄漏oom。
     */
    @Override
    public void onDestory() {
        if (mIView != null) {

            mIView = null;
        }
    }

    @Override
    public void onItemClick(int position) {
        if (mIView != null) {

            mIView.showItemMsg("点击了第"+position+"项");
        }
    }

    @Override
    public void finished(List<String> list) {
        //最后加载完数据，显示数据，隐藏progressbar
        mIView.hideProgress();

        //从model层传递数据过来
        mIView.setItemData(list);

    }
}
