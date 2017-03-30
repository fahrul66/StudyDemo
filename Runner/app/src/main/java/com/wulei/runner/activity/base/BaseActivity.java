package com.wulei.runner.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.wulei.runner.utils.FragmentUtils;

import butterknife.ButterKnife;

/**
 * Created by wulei on 2017/1/13.
 */

public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 本activity的fragment
     */
    Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置layoutView
        setContentView(getLayoutId());
        //初始化ButterKnife
        ButterKnife.bind(this);
        //初始化默认fragment
        initFragment(getFragmentTag());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeFragment();
    }

    /**
     * 默认初始化fragment
     */
    private void initFragment(String tag) {
        if (tag != null) {
            //创建
            fragment = FragmentUtils.newInstance(tag);
            if (fragment != null) {
                //添加
                FragmentUtils.add(this, fragment, tag);
            }
        }

    }

    /**
     * 清空回退栈中的所有fragment
     */
    private void removeFragment() {
        FragmentUtils.clearBackStack(this);
    }

    /**
     * 获得fragment的标识，TAG
     *
     * @return 返回fragment的TAG
     */
    public abstract String getFragmentTag();

    /**
     * 返回activity的布局
     * @return 返回的布局id
     */
    public abstract int getLayoutId();
}
