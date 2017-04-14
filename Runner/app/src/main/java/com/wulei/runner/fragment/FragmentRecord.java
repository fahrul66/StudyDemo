package com.wulei.runner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wulei.runner.R;
import com.wulei.runner.activity.MainActivity;
import com.wulei.runner.app.App;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.utils.DividerItemDecoration;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/01.
 */

public class FragmentRecord extends BaseFragment {
    @BindView(R.id.coordinate_record)
    CoordinatorLayout mCoordinate;
    @BindView(R.id.fab_record)
    FloatingActionButton mFab;
    @BindView(R.id.recycler_record)
    RecyclerView mRecyclerView;
    //数据库helper
    private LocalSqlHelper lsh;

    @NonNull
    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //数据库
        lsh = new LocalSqlHelper(App.mAPPContext);
        //初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
    /**
     * 弹出栈
     */
    @Override
    protected void onBackPressed() {
        //默认行为,返回栈
        mAppCompatActivity.getSupportFragmentManager().popBackStack();
        //toolbar返回
        ((MainActivity)mActivity).mNavigationView.setCheckedItem(R.id.run);
    }
}
