package com.wulei.funnypicture;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.wulei.funnypicture.Adapter.ViewPagerAdapter;
import com.wulei.funnypicture.fragment.Joke;
import com.wulei.funnypicture.fragment.Pics;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /**
     * Toolbar
     */
    private Toolbar toolbar;
    /**
     * design包中的tabLayout布局,与item
     */
    private TabLayout tabLayout;
    private TabItem jokes;
    private TabItem pics;
    /**
     * v4包的ViewPager
     */
    private ViewPager viewPager;
    /**
     * viewPager的标签
     */
    private String[] titles = {"笑话", "趣图"};
    /**
     * fragment集合
     */
    private List<Fragment> fragmentList = new ArrayList<>();
    /**
     * viewPager适配器
     */
    private ViewPagerAdapter viewPagerAdapter;

    /**
     * fragment
     */
    private Joke jokeFragment = new Joke();
    private Pics picsFragment = new Pics();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        init();
        //设置toolbar
        setSupportActionBar(toolbar);
        //添加fragment
        fragmentList.add(jokeFragment);
        fragmentList.add(picsFragment);
        //设置适配器
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(viewPagerAdapter);

        //tabLayout与ViewPager绑定
        setTitles(titles);
        tabLayout.setupWithViewPager(viewPager, true);

    }

    /**
     * 初始化控件
     */
    private void init() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        jokes = (TabItem) findViewById(R.id.jokes);
//        pics = (TabItem) findViewById(R.id.pics);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 给tablayout设置title
     */
    private void setTitles(String[] titles) {
        //循环遍历titles添加title
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }
    }

    /**
     * 网络
     */
    private void netWork(View v){
       ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !(networkInfo.isAvailable())){
//            Snackbar.make(v,"网络异常", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        netWork(getWindow().getDecorView());
    }
}
