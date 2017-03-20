package com.wulei.runner.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.fragment.MenuFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNavigationView;

    /**
     * fragment实例
     */
    Fragment fragmentMain = new MenuFragment();
    Fragment fragmentSeach = new MenuFragment();
    Fragment fragmentNotification = new MenuFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * butterKnife三方库绑定
         */
        ButterKnife.bind(this);

        /**
         * 传递参数
         */
        Bundle bundle = new Bundle();
        bundle.putInt("PARA",1);
        fragmentMain.setArguments(bundle);
        bundle.clear();

        bundle.putInt("PARA",2);
        fragmentSeach.setArguments(bundle);
        bundle.clear();

        bundle.putInt("PARA",3);
        fragmentNotification.setArguments(bundle);
        bundle.clear();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        clickFragmentManager(fragmentMain);
                        break;
                    case R.id.menu_notifications:
                        clickFragmentManager(fragmentSeach);
                        break;
                    case R.id.menu_search:
                        clickFragmentManager(fragmentNotification);
                        break;
                }
                return true;
            }
        });

    }

    /**
     * 点击事件的管理器
     * @param fragment
     */
    private void clickFragmentManager(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_main,fragment);
        fragmentTransaction.commit();
    }
}
