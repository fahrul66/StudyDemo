package com.work.wulei.myapplication;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.work.wulei.Presenter.IPresenter;
import com.work.wulei.Presenter.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements IView, View.OnClickListener {
    private IPresenter mIPresenter;
    private ProgressDialog progressDialog;
    private Button login, clear;
    private EditText user, password;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.login_activity_layout);

        //初始化
        mIPresenter = new Presenter(this);
        login = (Button) findViewById(R.id.login);
        clear = (Button) findViewById(R.id.clear);
        login.setOnClickListener(this);
        clear.setOnClickListener(this);

        user = (EditText) findViewById(R.id.edit_user);
        password = (EditText) findViewById(R.id.edit_pwd);


        /*//测试程序的backpressure
        Subscription s = Observable.interval(1,TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Long>() {
                               @Override
                               public void onStart() {
                                   super.onStart();
                                   //reactive-pull 响应式拉取，observer主动获取数据
                                   Log.i(TAG, "onStart: start");
                                   request(1);
                                   Log.i(TAG, "onStart: end");
                               }

                               @Override
                               public void onCompleted() {
                                   Log.i(TAG, "onCompleted: ");
                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.i(TAG, "onError: ");
                               }

                               @Override
                               public void onNext(Long aLong) {

                                   try {
                                       Thread.sleep(1000);
                                   } catch (InterruptedException e) {
                                       e.printStackTrace();
                                   }
                                   Log.i(TAG, "onNext: " + aLong);
                                   //处理完事件后，再次发出请求

                               }
                           }
                );
*/
    }

    /**
     * 点击事件
     */
/*    private void onLogin() {
      mIPresenter.login();
    }

    //清楚
    private void onClear() {
     mIPresenter.clear();
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIPresenter.onDestory();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "show", "加载中...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public String getUser() {
        return user.getText().toString();
    }

    @Override
    public String getPWD() {
        return password.getText().toString();
    }


    @Override
    public void clear() {
        user.setText("");
        password.setText("");

        user.setError("");
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        mIPresenter.clear();
        switch (v.getId()) {
            case R.id.login:
                mIPresenter.login();
                //转场动画
                ImageView imageView = (ImageView) findViewById(R.id.MFCirImageView);
                ActivityOptionsCompat aoptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, getString(R.string.app_name));
                //跳转
                startActivity(new Intent(this, RecyclerActivity.class), aoptions.toBundle());
                break;
            case R.id.clear:
                break;
        }
    }
}
