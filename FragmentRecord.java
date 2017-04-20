package com.wulei.runner.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wulei.runner.R;
import com.wulei.runner.activity.MainActivity;
import com.wulei.runner.adapter.RecordAdapter;
import com.wulei.runner.app.App;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.model.LocalSqlRun;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/01.
 */

public class FragmentRecord extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.coordinate_record)
    CoordinatorLayout mCoordinate;
    @BindView(R.id.fab_record)
    FloatingActionButton mFab;
    @BindView(R.id.recycler_record)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh_record)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.empty_data)
    RelativeLayout mlayout;
    @BindView(R.id.img_load)
    ImageView mImage;
    //数据库helper
    private LocalSqlHelper lsh;
    //数据
    List<LocalSqlRun> mList;
    private RecordAdapter adapter;

    /**
     * 布局绑定
     *
     * @return
     */
    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_record;
    }

    /**
     * 数据初始化
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        //数据库
        lsh = new LocalSqlHelper(App.mAPPContext);
        //初始化
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(ConstantFactory.SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        mList = lsh.queryPB(null);
        adapter = new RecordAdapter(mActivity, mList);

        mlayout.setVisibility(View.GONE);
        hasData();


    }

    private void hasData() {
        //判断是否无数据
        if (mList.isEmpty() || mList == null) {
            showEmpty();
        } else {
            mRecyclerView.setAdapter(adapter);
            hideLayout();
        }
    }

    private void showEmpty() {
        ////开启动画
        AnimationDrawable a = (AnimationDrawable) mImage.getBackground();
        a.start();
        mlayout.setVisibility(View.VISIBLE);
    }

    private void hideLayout() {
        mlayout.setVisibility(View.GONE);
    }

    /**
     * 监听器设置
     */
    @Override
    protected void setListener() {
        mSrl.setOnRefreshListener(this);
    }

    /**
     * 逻辑代码处理
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    /**
     * 弹出栈
     */
//    @Override
//    protected void onBackPressed() {
//        //默认行为,返回栈
//        ////判断是否无数据
//        mAppCompatActivity.getSupportFragmentManager().popBackStack();
//        //toolbar返回
//        ((MainActivity) mActivity).mNavigationView.setCheckedItem(R.id.run);
//    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        mSrl.setRefreshing(true);
        mList = lsh.queryPB(null);
        adapter = new RecordAdapter(mActivity, mList);
        adapter.notifyDataSetChanged();

        ////判断是否无数据
        hasData();
        mSrl.setRefreshing(false);
    }
}
