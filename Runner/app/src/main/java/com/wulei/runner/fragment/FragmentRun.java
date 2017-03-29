package com.wulei.runner.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wulei.runner.R;
import com.wulei.runner.fragment.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wulei on 2017/3/27.
 */

public class FragmentRun extends BaseFragment {

    @BindView(R.id.toolbar_run)
    Toolbar mToolbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mAppCompatActivity.setSupportActionBar(mToolbar);
    }

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_run;
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
