package com.wulei.runner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.wulei.runner.R;
import com.wulei.runner.activity.MainActivity;
import com.wulei.runner.fragment.base.BaseFragment;

/**
 * Created by wule on 2017/04/01.
 */

public class FragmentNews extends BaseFragment {

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_news;
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

    @Override
    protected void onBackPressed() {
        //默认行为,返回栈
        mAppCompatActivity.getSupportFragmentManager().popBackStack();
        //toolbar返回
        ((MainActivity)mActivity).mNavigationView.setCheckedItem(R.id.run);
    }
}
