package com.wulei.runner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import com.wulei.runner.R;
import com.wulei.runner.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/01.
 */

public class FragmentGoal extends BaseFragment {
    @BindView(R.id.recycler_goals)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab_goals)
    FloatingActionButton mFab;
    @BindView(R.id.coordinate_goals)
    CoordinatorLayout mCoordi;

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_goals;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
