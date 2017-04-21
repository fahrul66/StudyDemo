package com.wulei.runner.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.model.User;
import com.wulei.runner.utils.SnackbarUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by wule on 2017/04/01.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.coordinate_login)
    CoordinatorLayout mCoordinate;
    @BindView(R.id.mobile_layout)
    TextInputLayout mMobileLayout;
    @BindView(R.id.pw_layout)
    TextInputLayout mPwLayout;
    @BindView(R.id.pw)
    TextInputEditText mPw;
    @BindView(R.id.mobile)
    TextInputEditText mPhone;
    @BindView(R.id.btn_login)
    Button mLogin;
    @BindView(R.id.tv_find_pw)
    TextView mFindPw;
    @BindView(R.id.tv_register)
    TextView mregister;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //设置是布局可以伸展到status bar上
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }


    /**
     * 登陆
     */
    @OnClick(R.id.btn_login)
    public void login() {
        //清除状态
        mMobileLayout.setErrorEnabled(false);
        mPwLayout.setErrorEnabled(false);
        //获取数据
        String phone = mPhone.getText().toString();
        String password = mPw.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            mMobileLayout.setError("输入不能为空");
        } else if (TextUtils.isEmpty(password)) {
            mPwLayout.setError("输入不能为空");
        } else {
            BmobUser.loginByAccount(phone, password, new LogInListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (user == null) {
                        //错误判断
                        SnackbarUtil.show(mCoordinate, "登陆失败！" + e.getMessage());
                    } else {
                        finish();
                    }
                }
            });
        }
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.tv_find_pw)
    public void findPw() {
        startActivity(new Intent(this, FindPwActivity.class));
    }

    /**
     * 注册
     */
    @OnClick(R.id.tv_register)
    public void register() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    protected void hideShowFragment() {
    }

    /**
     * 首次初始化登录
     *
     * @return
     */
    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
}
