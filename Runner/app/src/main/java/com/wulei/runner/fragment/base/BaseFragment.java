package com.wulei.runner.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.app.App;
import com.wulei.runner.borcastReceiver.NetworkReceiver;
import com.wulei.runner.utils.ToastUtil;

import butterknife.ButterKnife;


/**
 * Created by wule on 2017/03/21.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 获得fragment的寄存的activity对象
     */
    protected Activity mActivity;
    protected AppCompatActivity mAppCompatActivity;
    /**
     * 广播接收器
     */
    private NetworkReceiver mNetworkReceiver;
    private IntentFilter mIntentFilter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
        mAppCompatActivity = (AppCompatActivity) context;
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
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
        return inflater.inflate(setContentView(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //绑定此fragment
        ButterKnife.bind(this, view);
        //初始化view
        initView(savedInstanceState);
        //设置监听器
        setListener();
        //处理逻辑问题
        processLogic(savedInstanceState);
        //注册广播
        mActivity.registerReceiver(mNetworkReceiver, mIntentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁广播
        mActivity.unregisterReceiver(mNetworkReceiver);
    }

    /**
     * 设置fragment的resID
     *
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


}
