package com.example.wulei.demotest;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerCardView extends AppCompatActivity implements RecyclerViewAdapter.OnMyClickListener {
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //recyclerView的数据集合
    private List<String> mData = new ArrayList<>();
    //适配器
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_card_view);
        //初始化控件
        init();
        //设置toolbar为actionbar
        setSupportActionBar(toolbar);
        //下拉刷新设置
        swipeRefreshSetup();
        //recyclerView设置
        recyclerViewSetup();
    }

    /**
     * recyclerView的相关设置
     */
    private void recyclerViewSetup() {
        //初始化数据
        initData();
        //设置布局管理器,context,水平还是垂直布局，（几行或几列），是否倒过来显示。
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false));
        /**
         * 注意横向滑动是item的宽要设置
         */
          recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        //设置itemDecoration,分割线,接着咋属性中设置listDivider
//        recyclerView.addItemDecoration(new LinearLayoutItemDecoration(this, LinearLayoutItemDecoration.VERTICAL_LIST));
//          recyclerView.addItemDecoration(new GridLayoutItemDecoration(this));
        //设置动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        adapter = new RecyclerViewAdapter(this, mData);
        recyclerView.setAdapter(adapter);
        //监听
        adapter.setMyOnclickListener(this);
    }

    /**
     * 初始化RecyclerView的数据
     */
    private void initData() {
        for (int i = 0; i <= 50; i++) {
            mData.add("第" + i + "个数据");
        }
    }

    /**
     * 下拉刷新设置
     */
    private void swipeRefreshSetup() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //设置刷新的颜色
                swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light
                        , android.R.color.holo_orange_light, android.R.color.holo_purple);
                //延迟4秒，关闭progressbar控件
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        //刷新后sanckBar出现
                        showSnackBar();
                    }
                }, 4000);
            }
        });
    }

    /**
     * sanckbar对象
     */
    private void showSnackBar() {
        View v = findViewById(R.id.activity_recycler_card_view);
        Snackbar.make(v, "添加一条数据", Snackbar.LENGTH_LONG).setAction("添加", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向RecyclerView中添加一条数据
                mData.add(1,"添加的数据");
                //唤醒适配器更新数据
                //adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(1);


            }
        }).show();
    }

    /**
     * 控件初始化
     */
    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRLRecycleCV);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        toolbar = (Toolbar) findViewById(R.id.toolbar_RecyclerView);
    }

    /**
     * 接口回调响应
     * @param view
     * @param position
     */
    @Override
    public void onClick(View view, int position) {
        Toast.makeText(this,"我是Onclick"+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(View view, int position) {
        Toast.makeText(this,"我是LongClick"+position,Toast.LENGTH_SHORT).show();
    }
}
