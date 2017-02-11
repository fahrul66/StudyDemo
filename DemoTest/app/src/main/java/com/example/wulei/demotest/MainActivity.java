package com.example.wulei.demotest;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private FloatingActionButton fab;
    //navigation对象
    private NavigationView navigationView;
    //drawerLayout对象
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //设置是布局可以伸展到status bar上
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        listView = (ListView) findViewById(R.id.lv);
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getData());
//        listView.setAdapter(adapter);

        //fab监听事件
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //CoordiantorLayout viewTree
        final View vv = findViewById(R.id.coordinator);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make方法中的View必须是根布局的decorView或者CoordiantorLayout的View,fab才会自动上移
                Snackbar.make(vv, "我是sanckbar", Snackbar.LENGTH_SHORT)
                        .setAction("添加", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "add a msg", Toast.LENGTH_SHORT).show();
                            }
                        }).show();

            }
        });

        //初始化控件
        init();
        //toolbar的navigation点击事件
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        DrawerArrowDrawable d = new DrawerArrowDrawable(this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //同步状态
        actionBarDrawerToggle.syncState();
//        d.setColor(Color.WHITE);
//        toolbar.setNavigationIcon(new DrawerArrowDrawable(this));
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(navigationView);
//            }
//        });

    }

    /**
     * 初始化
     *
     * @return
     */
    private void init() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    }

    private ArrayList<String> getData() {
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            al.add("第" + i + "个数据");
        }
        return al;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setTitle("随意").setMessage("菜单关于").setPositiveButton("sure", null)
                        .setNegativeButton("cancle", null).setIcon(R.mipmap.ic_launcher).show();
                break;
            //跳转到Toolbar和collapsingToolbarLayout界面
            case R.id.menu3:
                startActivity(new Intent(this, TestMD.class));
                break;
            //跳转到recyclerView和CardView的Demo界面
            case R.id.menu4:
                startActivity(new Intent(this,RecyclerCardView.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
