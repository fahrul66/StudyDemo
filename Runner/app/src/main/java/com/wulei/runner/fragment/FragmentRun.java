package com.wulei.runner.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.wulei.runner.R;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.FragmentUtils;
import com.wulei.runner.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wulei on 2017/3/27.
 */

public class FragmentRun extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.toolbar_run)
    Toolbar mToolbar;
    @BindView(R.id.btn_start_run)
    Button mButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAppCompatActivity.setSupportActionBar(mToolbar);
    }

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_run;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //toolbar
    }

    @Override
    protected void setListener() {
        mButton.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
           Fragment fragment = FragmentUtils.newInstance(ConstantFactory.TAG_MAP);
           FragmentUtils.hide(mActivity,this);
           FragmentUtils.add(mActivity,fragment,ConstantFactory.TAG_MAP);

           //权限问题
    }
}
