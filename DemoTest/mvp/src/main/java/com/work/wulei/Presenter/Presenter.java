package com.work.wulei.Presenter;

import android.os.Handler;
import android.text.TextUtils;

import com.work.wulei.myapplication.IView;


/**
 * Created by wulei on 2016/12/28.
 */

public class Presenter implements IPresenter {
    //view的引用
    private IView mIView;

    public Presenter(IView mIView) {
        super();
        this.mIView = mIView;
    }

    @Override
    public void login() {
        if (mIView != null) {
            if (isRight(mIView.getUser(), mIView.getPWD())) {
                //页面跳转
                mIView.showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIView.hideProgress();
                    }
                }, 1000);
            } else {
                //
                mIView.showToast("登陆失败");
            }

        }

    }

    private boolean isRight(String user, String password) {
        if (TextUtils.isEmpty(user) | TextUtils.isEmpty(password)) {
            mIView.showToast("输入不能为空！");
            return false;
        } else if (user.length() < 6 && TextUtils.isDigitsOnly(password)) {
            mIView.showToast("用户名不得少于6位数，密码只能为数字");
            return false;
        } else{
            mIView.showToast("登陆成功");
            return true;
        }
    }

    @Override
    public void clear() {
        if (mIView != null) {
            mIView.clear();
        }
    }

    @Override
    public void onDestory() {
        if (mIView != null) {
            mIView = null;
        }
    }
}
