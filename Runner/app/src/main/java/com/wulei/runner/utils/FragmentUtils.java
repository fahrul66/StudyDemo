package com.wulei.runner.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.fragment.FragmentEmpty;
import com.wulei.runner.fragment.FragmentGoal;
import com.wulei.runner.fragment.FragmentNews;
import com.wulei.runner.fragment.FragmentRank;
import com.wulei.runner.fragment.FragmentRecord;
import com.wulei.runner.fragment.FragmentRun;
import com.wulei.runner.fragment.FragmentSetting;
import com.wulei.runner.fragment.FragmentShare;
import com.wulei.runner.fragment.FragmentSuggest;

import java.util.List;

/**
 * Created by wulei on 2017/3/27.
 * 主要用来添加，替换，隐藏，显示工作
 */

public class FragmentUtils {

    /**
     * 所有的fragment，实例，单例模式，懒汉式
     */
    private static Fragment mFragmentRun = new FragmentRun();
    private static Fragment mFragmentNews = new FragmentNews();
    private static Fragment mFragmentGoal = new FragmentGoal();
    private static Fragment mFragmentRank = new FragmentRank();
    private static Fragment mFragmentRecord = new FragmentRecord();
    private static Fragment mFragmentSetting = new FragmentSetting();
    private static Fragment mFragmentShare = new FragmentShare();
    private static Fragment mFragmentSuggest = new FragmentSuggest();
    private static Fragment mFragmentEmpty = new FragmentEmpty();

    /**
     * 根据tag标记，返回fragment。
     *
     * @param tag
     * @return
     */
    public static Fragment newInstance(String tag) {
        Fragment fragment = null;
        switch (tag) {
            case ConstantFactory.TAG_RUN:
                fragment = mFragmentRun;
                break;
            case ConstantFactory.TAG_NEWS:
                fragment = mFragmentNews;
                break;
            case ConstantFactory.TAG_GOAL:
                fragment = mFragmentGoal;
                break;
            case ConstantFactory.TAG_RANK:
                fragment = mFragmentRank;
                break;
            case ConstantFactory.TAG_RECORD:
                fragment = mFragmentRecord;
                break;
            case ConstantFactory.TAG_SETTING:
                fragment = mFragmentSetting;
                break;
            case ConstantFactory.TAG_SHARE:
                fragment = mFragmentShare;
                break;
            case ConstantFactory.TAG_SUGGEST:
                fragment = mFragmentSuggest;
                break;
            case ConstantFactory.TAG_Empty:
                fragment = mFragmentEmpty;
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
     *
     * @param context        上下文
     * @param fragment       要替换的fragment
     * @param tag            fragment的标识tag
     * @param addToBackStack 是否添加到返回栈中
     */
    public static void replace(Context context, Fragment fragment, String tag, boolean addToBackStack) {
        FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
        Fragment target = fm.findFragmentByTag(tag);
        //判断fragmentManager对象池中是否有此tag,没有时才再次添加。
        if (target == null) {
            //target = newInstance(tag);
            //fragmentManager开启事务添加fragment
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content, fragment, tag);
            //添加到返回栈中
            if (addToBackStack) {
                ft.addToBackStack(null);
            }
            ft.commit();
        }
    }

    /**
     * 隐藏此fragment
     *
     * @param context
     * @param fragment
     */
    public static void hide(Context context, Fragment fragment) {
        //如果fragment状态，则显示。
        if (fragment.isVisible()) {
            //判断fragmentManager的对象池中的fragment，是否都隐藏
            FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
            //fragmentManager开启事务隐藏fragment
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fragment);
            ft.commit();
        }
    }

    /**
     * 显示此fragment
     *
     * @param context
     * @param fragment
     */
    public static void show(Context context, Fragment fragment) {
        //如果fragment隐藏状态，则显示。
        if (fragment.isHidden()) {
            FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
            //fragmentManager开启事务隐藏fragment
            FragmentTransaction ft = fm.beginTransaction();
            ft.show(fragment);
            ft.commit();
        }
    }


    /**
     * 清空回退栈
     *
     * @param context
     */
    public static void clearBackStack(Context context) {

        FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
        List<Fragment> fmFragments = fm.getFragments();
        int backStackCount = fm.getBackStackEntryCount();
        if (backStackCount > 1) {
            for (int i = 0; i < backStackCount; i++) {
                fm.popBackStack();
            }
        }
    }
}
