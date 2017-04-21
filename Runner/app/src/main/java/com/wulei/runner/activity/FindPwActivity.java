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
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.SnackbarUtil;
import com.wulei.runner.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by wule on 2017/04/20.
 */

public class FindPwActivity extends BaseActivity {

    @BindView(R.id.coordinate_find)
    CoordinatorLayout mCoordinate;
    @BindView(R.id.btn_find)
    Button mRegister;
    @BindView(R.id.pw_layout_find)
    TextInputLayout mPwlL;
    @BindView(R.id.pw_new_layout_find)
    TextInputLayout mPwConfirmL;
    @BindView(R.id.mobile_layout_find)
    TextInputLayout mPhoneL;
    @BindView(R.id.mobile_find)
    TextInputEditText mPhone;
    @BindView(R.id.pw_find)
    TextInputEditText mPw;
    @BindView(R.id.pw_new_pw)
    TextInputEditText mPwConfirm;
    @BindView(R.id.btn_find_sms_code)
    Button mSms;

    private String id;

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
    @OnClick(R.id.btn_find)
    public void register() {
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("注册中...");
        p.setCanceledOnTouchOutside(false);
        //清除状态
        clearError();
        //获取密码
        final String phone = mPhone.getText().toString();
        String pw = mPw.getText().toString();
        final String pwConfirm = mPwConfirm.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            mPhoneL.setError("输入不能为空");
        } else if (TextUtils.isEmpty(pw)) {
            mPwlL.setError("输入不能为空");
        } else if (TextUtils.isEmpty(pwConfirm)) {
            mPwConfirmL.setError("输入不能为空");
        } else if (pw.equals(pwConfirm)) {
            mPwConfirmL.setError("两次输入的密码一致");
        } else {
            p.show();
            //验证码验证
            BmobSMS.verifySmsCode(phone, pw, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //更新数据
                        BmobQuery<User> query = new BmobQuery<User>();
                        query.addWhereEqualTo(ConstantFactory.PHONE_NUM, phone);
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> list, BmobException e) {
                                if (e == null) {
                                    id = list.get(0).getObjectId();
                                } else {
                                    SnackbarUtil.show(mCoordinate, "您没有绑定手机" + e.getMessage());
                                }
                            }
                        });
                        //更新
                        User u = new User();
                        u.setPassword(pwConfirm);
                        u.update(id, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    finish();
                                } else {
                                    SnackbarUtil.show(mCoordinate, "密码更新失败" + e.getMessage());
                                }
                            }
                        });
                    } else {
                        SnackbarUtil.show(mCoordinate, "验证码错误" + e.getMessage());
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

    /**
     * 获取验证码
     */
    @OnClick(R.id.btn_find_sms_code)
    public void getSms() {
        //清除状态
        clearError();
        String phone = mPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            mPhoneL.setError("输入不能为空");
        } else {
            BmobSMS.requestSMSCode(phone, ConstantFactory.SMS_TEMPLATE, new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e == null) {
                        SnackbarUtil.show(mCoordinate, "验证码发送成功");
                    } else {
                        SnackbarUtil.show(mCoordinate, "验证码发送失败" + e.getMessage());
                    }
                }
            });
        }
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
        return R.layout.activity_find_password;
    }
}
