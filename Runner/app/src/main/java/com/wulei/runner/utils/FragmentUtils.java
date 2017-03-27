package com.wulei.runner.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.fragment.FragmentRun;

/**
 * Created by wulei on 2017/3/27.
 * 主要用来添加，替换，隐藏，显示工作
 */

public class FragmentUtils {
    public static final String TAG_RUN = "run";
    public static final String TAG_GOAL = "goal";
    public static final String TAG_RECORD = "record";


    /**
     * 根据tag标记，创建fragment。
     *
     * @param tag
     * @return
     */
    public static Fragment newInstance(String tag) {
        Fragment fragment = null;
        switch (tag) {
            case TAG_RUN:
                fragment = new FragmentRun();
                break;
        }
        return fragment;
    }

    /**
     * fragment的添加类
     */
    public static void add(Context context, Fragment fragment, String tag) {
        FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
        Fragment target = fm.findFragmentByTag(tag);
        //判断fragmentManager对象池中是否有此tag,没有时才再次添加。
        if (target == null) {
            //target = newInstance(tag);
            //fragmentManager开启事务添加fragment
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.content, fragment, tag);
            ft.commit();
        }
    }

    /**
     * 替换fragment
     * @param context 上下文
     * @param fragment 要替换的fragment
     * @param tag fragment的标识tag
     * @param addToBackStack 是否添加到返回栈中
     */
    public static void repalce(Context context,Fragment fragment,String tag,boolean addToBackStack) {
        FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
        Fragment target = fm.findFragmentByTag(tag);
        //判断fragmentManager对象池中是否有此tag,没有时才再次添加。
        if (target == null) {
            //target = newInstance(tag);
            //fragmentManager开启事务添加fragment
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content, fragment, tag);
            //添加到返回栈中
            if (addToBackStack){
               ft.addToBackStack(null);
            }
            ft.commit();
        }
    }

    /**
     * 隐藏此fragment
     * @param context
     * @param fragment
     */
    public static void hide(Context context,Fragment fragment) {
        FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
            //fragmentManager开启事务隐藏fragment
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fragment);
            ft.commit();
    }

    /**
     * 显示此fragment
     * @param context
     * @param fragment
     */
    public static void show(Context context,Fragment fragment) {
        FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
        //fragmentManager开启事务隐藏fragment
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }
}
