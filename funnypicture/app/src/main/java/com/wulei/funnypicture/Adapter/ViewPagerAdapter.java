package com.wulei.funnypicture.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wulei on 2017/2/9.
 */
//fragmentStatePagerAdapter会销毁fragment实例，然后重新创建回复数据，适用于大量的fragment
//fragmentPagerAdapter会销毁view不会销毁fragment，适用于少量的fragment
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    /**
     * fragment集合
     */
    private List<Fragment> fragmentList = new ArrayList<>();
    /**
     * viewPager标签
     */
    private String[] titles = new String[10];

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,String[] titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
