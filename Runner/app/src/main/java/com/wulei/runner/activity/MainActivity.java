package com.wulei.runner.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.fragment.FragmentGoal;
import com.wulei.runner.fragment.FragmentNews;
import com.wulei.runner.fragment.FragmentRank;
import com.wulei.runner.fragment.FragmentRecord;
import com.wulei.runner.fragment.FragmentRun;
import com.wulei.runner.fragment.FragmentSetting;
import com.wulei.runner.fragment.FragmentShare;
import com.wulei.runner.fragment.FragmentSuggest;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.FragmentUtils;
import com.wulei.runner.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import com.wulei.runner.R;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final int TAG = 1;
    //flag,退出
    private boolean isQuit = false;
    //toolbar
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //抽屉
    @BindView(R.id.drawer)
    public DrawerLayout mDrawerLayout;
    //抽屉view
    @BindView(R.id.navigation)
    public NavigationView mNavigationView;
    TextView mTv;
    ImageView mImage;
    //运动的fragment
    private FragmentRun mFragmentRun = (FragmentRun) FragmentUtils.newInstance(ConstantFactory.TAG_RUN);
    private FragmentNews mFragmentNews = (FragmentNews) FragmentUtils.newInstance(ConstantFactory.TAG_NEWS);
    private FragmentGoal mFragmentGoal = (FragmentGoal) FragmentUtils.newInstance(ConstantFactory.TAG_GOAL);
    private FragmentRank mFragmentRank = (FragmentRank) FragmentUtils.newInstance(ConstantFactory.TAG_RANK);
    private FragmentRecord mFragmentRecord = (FragmentRecord) FragmentUtils.newInstance(ConstantFactory.TAG_RECORD);
    private FragmentSetting mFragmentSetting = (FragmentSetting) FragmentUtils.newInstance(ConstantFactory.TAG_SETTING);
    private FragmentShare mFragmentShare = (FragmentShare) FragmentUtils.newInstance(ConstantFactory.TAG_SHARE);
    private FragmentSuggest mFragmentSuggest = (FragmentSuggest) FragmentUtils.newInstance(ConstantFactory.TAG_SUGGEST);
    //判断是否是null
    private boolean isGoalNull = false;
    //request code
    public static final int REQUEST = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //导航栏监听
        mNavigationView.setNavigationItemSelectedListener(this);

        //toolbar设置
        setSupportActionBar(mToolbar);
        //添加navigation icon
        ActionBarDrawerToggle abt = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.toolbar_drawer_open, R.string.toolbar_drawer_close);
        mDrawerLayout.addDrawerListener(abt);
        abt.syncState();

        //toolbar监听器
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mToolbar.setTitle(getResources().getString(R.string.menu_goal));

        //header click
        initHeader();


    }

    /**
     * 初始化header
     */
    private void initHeader() {
        View header = mNavigationView.getHeaderView(0);
        mTv = (TextView) header.findViewById(R.id.btn_navigation_header);
        mImage = (ImageView) header.findViewById(R.id.img_navigation_header);
        mTv.setOnClickListener(this);
    }

    /**
     * activity重启异常，fragment重叠，处理hide，show
     */
    @Override
    protected void hideShowFragment() {
        FragmentRun fragmentRun = null;
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof FragmentRun) {
                fragmentRun = (FragmentRun) fragment;
            }
            // 解决重叠问题
            getSupportFragmentManager().beginTransaction()
                    .show(fragmentRun)
                    .commit();


        }
    }

    /**
     * activity默认初始化的fragment
     *
     * @return
     */
    @Override
    public String getFragmentTag() {
        return ConstantFactory.TAG_RUN;
    }

    /**
     * activity默认加载根view
     *
     * @return
     */
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
                //
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, mFragmentRun, ConstantFactory.TAG_RUN)
                        .commit();
//                FragmentUtils.replace(this, mFragmentRun, ConstantFactory.TAG_RUN, false);
                mNavigationView.setCheckedItem(R.id.run);
                mToolbar.setTitle(getResources().getString(R.string.menu_run));
                break;
            case R.id.goal:
                FragmentUtils.replace(this, mFragmentGoal, ConstantFactory.TAG_GOAL, true);
                mNavigationView.setCheckedItem(R.id.goal);
                mToolbar.setTitle(getResources().getString(R.string.menu_goal));
                break;
            case R.id.record:
                FragmentUtils.replace(this, mFragmentRecord, ConstantFactory.TAG_RECORD, true);
                mNavigationView.setCheckedItem(R.id.record);
                mToolbar.setTitle(getResources().getString(R.string.menu_record));
                break;
            case R.id.news:
                FragmentUtils.replace(this, mFragmentNews, ConstantFactory.TAG_NEWS, true);
                mNavigationView.setCheckedItem(R.id.news);
                mToolbar.setTitle(getResources().getString(R.string.menu_news));
                break;
            case R.id.rank:
                mNavigationView.setCheckedItem(R.id.rank);
                mToolbar.setTitle(getResources().getString(R.string.menu_rank));
                break;
            case R.id.setting:
                break;
            case R.id.shard:
                break;
            case R.id.suggest:
                break;

        }
        //关闭抽屉
        mDrawerLayout.closeDrawers();
        return false;
    }


    /**
     * handler处理退出的消息
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TAG) {
                isQuit = false;
            }
        }
    };

    /**
     * 重写返回按钮，设置为再按一次退出程序
     */
    @Override
    public void onBackPressed() {
        /*
         * 如果抽屉打开就关闭，然后返回，不执行下面的语句
         */
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            //抽屉打开时，按下返回键关闭
            mDrawerLayout.closeDrawers();
            return;
        }

        /*
         * 如果抽屉关闭，则执行确认操作。
         */
        if (isQuit && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            finish();
        } else {
            mHandler.sendEmptyMessageDelayed(TAG, 2000);
            ToastUtil.show(R.string.toast_quit);
            isQuit = true;
        }

    }

    /**
     * 点击事件
     *
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_navigation_header:
                //有无账号登陆
                if (BmobUser.getCurrentUser() == null) {
                    //跳转登陆
//                    View view = LayoutInflater.from(this).inflate(R.layout.activity_login, null);
//                    Bundle bundle = ActivityOptionsCompat.makeClipRevealAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
                    startActivityForResult(new Intent(this, LoginActivity.class), REQUEST);
                } else {
                    //跳转到个人设置

                }
                break;
        }
    }

    /**
     * 获取user的账号信息
     */
    @Override
    protected void onResume() {
        super.onResume();
        //没登陆，就不获取
        if (BmobUser.getCurrentUser() != null) {
            String username = (String) BmobUser.getObjectByKey(ConstantFactory.USERNAME);
            mTv.setText(username);
            BmobFile bmobFile= (BmobFile) BmobUser.getObjectByKey(ConstantFactory.ICON);
            bmobFile.getUrl();
//
        }

    }
}
