package com.wulei.runner.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.app.App;


/**
 * Created by wule on 2017/03/21.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected Activity mActivity;
    protected AppCompatActivity mAppCompatActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
        mAppCompatActivity = (AppCompatActivity) activity;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onUserVisible();
        } else {
            onUserInVisible();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);

        return  inflater.inflate(setContentView(), container, false);
    }



    /**
     * 设置fragment的resID
     * @return 返回布局的id
     */
    @NonNull
    @LayoutRes
    protected abstract int setContentView();


    /**
     * 初始化View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 当fragment对用户可见时，会调用该方法，可在该方法中懒加载网络数据
     */
    public void onUserVisible() {
    }

    /**
     * 当fragment对用户不可见时，会调用该方法
     */
    public void onUserInVisible() {
    }

    /**
     * 需要处理点击事件时，重写该方法
     *
     * @param v
     */
    public void onClick(View v) {
    }
}
