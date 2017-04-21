package com.wulei.runner.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.model.User;
import com.wulei.runner.utils.SnackbarUtil;
import com.wulei.runner.utils.ToastUtil;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wule on 2017/04/20.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.coordinate_register)
    CoordinatorLayout mCoordinate;
    @BindView(R.id.btn_register)
    Button mRegister;
    @BindView(R.id.pw_layout_register)
    TextInputLayout mPwlL;
    @BindView(R.id.pw_confirm_layout)
    TextInputLayout mPwConfirmL;
    @BindView(R.id.mobile_layout_register)
    TextInputLayout mPhoneL;
    @BindView(R.id.mobile_register)
    TextInputEditText mPhone;
    @BindView(R.id.pw_register)
    TextInputEditText mPw;
    @BindView(R.id.pw_confirm)
    TextInputEditText mPwConfirm;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //设置是布局可以伸展到status bar上
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * 注册
     */
    @OnClick(R.id.btn_register)
    public void register() {
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("注册中...");
        p.setCanceledOnTouchOutside(false);
        //清除状态
        clearError();
        //获取密码
        String phone = mPhone.getText().toString();
        String pw = mPw.getText().toString();
        String pwConfirm = mPwConfirm.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            mPhoneL.setError("输入不能为空");
        } else if (TextUtils.isEmpty(pw)) {
            mPwlL.setError("输入不能为空");
        } else if (TextUtils.isEmpty(pwConfirm)) {
            mPwConfirmL.setError("输入不能为空");
        } else if (!pw.equals(pwConfirm)){
            mPwConfirmL.setError("两次输入的密码不一致");
        } else {
            p.show();
            BmobUser user = new BmobUser();
            user.setUsername(phone);
            user.setPassword(pw);
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (user==null){
                        SnackbarUtil.show(mCoordinate,"注册失败"+e.getMessage());
                        p.dismiss();
                    } else {
                        ToastUtil.show("注册成功");
                        p.dismiss();
                        finish();
                    }
                }
            });
        }
    }


    /**
     * 清除edittext的状态
     */
    private void clearError() {
        mPhoneL.setErrorEnabled(false);
        mPwlL.setErrorEnabled(false);
        mPwConfirmL.setErrorEnabled(false);
    }

    @Override
    protected void hideShowFragment() {

    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }
}
