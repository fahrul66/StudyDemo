package com.wulei.runner.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.fragment.FragmentRun;
import com.wulei.runner.utils.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    //抽屉
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    //抽屉view
    @BindView(R.id.navigation)
    NavigationView mNavigationView;

    //运动的fragment
    FragmentRun mFragmentRun = (FragmentRun) FragmentUtils.newInstance(FragmentUtils.TAG_RUN);

    /**
     * fragment实例
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 注册监听器
         */
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public String getFragmentTag() {
        return FragmentUtils.TAG_RUN;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    /**
     * 抽屉的点击事件，fragment的切换
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.run:
                mNavigationView.setCheckedItem(R.id.run);
                break;
            case R.id.goal:
                mNavigationView.setCheckedItem(R.id.goal);
                break;
            case R.id.record:
                mNavigationView.setCheckedItem(R.id.record);
                break;
            case R.id.news:
                mNavigationView.setCheckedItem(R.id.news);
                break;
            case R.id.rank:
                mNavigationView.setCheckedItem(R.id.rank);
                break;
            case R.id.setting:
                break;
            case R.id.remind:
                break;
            case R.id.shard:
                break;
            case R.id.timing:
                break;
        }
        //关闭抽屉
        mDrawerLayout.closeDrawers();
        return false;
    }

}
