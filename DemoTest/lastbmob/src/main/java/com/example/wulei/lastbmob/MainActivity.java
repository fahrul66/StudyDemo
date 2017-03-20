package com.example.wulei.lastbmob;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import javaBean.UserBean;


public class MainActivity extends AppCompatActivity {
    public static final String APPLICATION_ID = "6907ba859c2f7e520391f282a401e5a4";

    private String objectId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 初始化Bmob
         */

        Bmob.initialize(this, APPLICATION_ID);

        test1();


    }

    private void test1() {
        final Button bu = (Button) findViewById(R.id.button);
        final BottomNavigationView b = (BottomNavigationView) findViewById(R.id.bttom);
        FloatingActionButton a = (FloatingActionButton) findViewById(R.id.fab);
        a.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                b.setVisibility(View.GONE);
                int centerX = v.getWidth()/2;
                int centerY = v.getHeight()/2;
              int max = Math.max(b.getWidth(),b.getHeight());
               Animator animator = ViewAnimationUtils.createCircularReveal(b,0,0,max/20,max);
                animator.start();
                b.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Bmob增加数据
     */
    void add(View v) {
        UserBean user = new UserBean();
        user.setName("wulei");
        user.setAddress("Ahui Bozhou");

        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    objectId = s;
                    Toast.makeText(MainActivity.this, "添加数据成功，返回objectId为：" + s, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 删除数据
     */
    void remove(View v) {
        final UserBean u =new UserBean();
        u.setAddress("安徽亳州");
        u.delete(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "删除成功"+u.getUpdatedAt(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "删除失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 更新数据
     */
    void update(View v) {
      UserBean u =new UserBean();
        u.setAddress("安徽亳州");
        u.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "更新成功" , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "更新失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 查询数据
     */
    void search(View v) {
        BmobQuery<UserBean> b = new BmobQuery<>();
        b.getObject(objectId, new QueryListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "查询成功" + userBean.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 点击事件
     */
    void click(View view) {
        startActivity(new Intent(this, ViewTest.class));
    }
}

