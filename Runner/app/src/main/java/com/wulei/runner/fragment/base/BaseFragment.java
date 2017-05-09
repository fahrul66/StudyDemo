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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wulei.runner.R;
import com.wulei.runner.activity.MainActivity;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.app.App;
import com.wulei.runner.borcastReceiver.NetworkReceiver;
import com.wulei.runner.utils.FragmentUtils;
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

    @Override
    public void onStart() {
        super.onStart();
//        /*
//         * 获得焦点，处理back事件
//         */
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    // handle back button's click listener
//
//                    onBackPressed();
//
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    /***
     * fragment中通过监听根view,实现返回按键
     */
    protected void onBackPressed() {
        //调用activity的onBackPressed方法
//         mAppCompatActivity.onBackPressed();
        //默认行为,返回栈
//        mAppCompatActivity.getSupportFragmentManager().popBackStack();
//        //toolbar返回
//        ((MainActivity)mActivity).mNavigationView.setCheckedItem(R.id.run);
    }

    /**
     * 此方法默认是true，在viewpager中使用时，在FragmentStateAdapter中系统调用，在fragment切换时
     * 默认调用，注意：viewpager默认初始化左右两个fragment。通过此方法进行懒加载。
     * 注意：当跳转到activity时，此方法不调用，还需要配合activity的resume和pasue生命周期方法
     * 进行fragment可见的判断
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            onUserVisible();
        } else {
            onUserInVisible();
        }
    }

    /**
     * 由于hide和show方法很特殊，fragment生命周期不会改变，但是在hide和show的时候会调用此方法。
     * 此方法是判断fragment是否是hide.
     * 注意：如果点击了home键，但是此方法不会调用，所以还需要和生命周期一起判断。
     *
     * @param hidden True if the fragment is now hidden, false otherwise
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //判断不隐藏时，并且处于resume状态时，可见
        if (!hidden && isResumed()) {
            onHiddenVisible();
        } else {
            onHiddenInVisible();
        }
    }

    /**
     * fragment is visible when show
     */
    public void onHiddenVisible() {
    }

    /**
     * fragment is invisible when hide
     */
    public void onHiddenInVisible() {
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
     * 前提是：viewpager
     * 当fragment对用户可见时，会调用该方法，可在该方法中懒加载网络数据
     */
    public void onUserVisible() {
    }

    /**
     * 前提是：viewpager
     * 当fragment对用户不可见时，会调用该方法
     */
    public void onUserInVisible() {
    }

}
