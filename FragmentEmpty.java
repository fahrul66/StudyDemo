package com.wulei.runner.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.wulei.runner.R;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.FragmentUtils;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/14.
 */

public class FragmentEmpty extends BaseFragment {

    @BindView(R.id.tv_empty)
    TextView mTv;
    @BindView(R.id.img_load)
    ImageView mImg;
    //传递的数据
    private String msg;

    /**
     * 传递数据
     *
     * @return
     */
    public static FragmentEmpty create(String str) {
        FragmentEmpty mFragmentEmpty = (FragmentEmpty) FragmentUtils.newInstance(ConstantFactory.TAG_EMPTY);
        Bundle b = new Bundle();
        b.putString(ConstantFactory.TAG_EMPTY, str);
        mFragmentEmpty.setArguments(b);
        return mFragmentEmpty;

    }

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_empty;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //开启动画
        AnimationDrawable a = (AnimationDrawable) mImg.getBackground();
        a.start();

        msg = getArguments().getString(ConstantFactory.TAG_EMPTY);
        //设置数据
        if (msg != null) {
            mTv.setText(msg);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
