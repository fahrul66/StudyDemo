package com.wulei.runner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.wulei.runner.fragment.MenuFragment;

import java.util.List;

/**
 * Created by wulei on 2017/1/30.
 */

public class ViewpagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list;
    FragmentManager fragmentManager;

    public ViewpagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);

        this.list = list;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        if (list.isEmpty() && list == null) {
             return new MenuFragment();
        }else {

            return list.get(position);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
