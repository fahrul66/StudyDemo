package com.example.wulei.retrofit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Second extends AppCompatActivity {
    private static final String TAG = "Second";
    private static final String KEY = "50a247fe6132b2019253b84134d7f062";
    private String sort = "desc";
    private int page = 2;
    private int pagesize = 20;
    private String time = timeToString(System.currentTimeMillis());
    /**
     * recyclerView 显示数据
     */
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    /**
     * 下拉刷新控件
     */
    @BindView(R.id.srl)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private AdapterR adapterR;

    /**
     * list数据
     *
     * @param savedInstanceState
     */
    private List<Datum> listAll = new ArrayList<>();
    /**
     * 返回的observable对象
     */
    private ResourceObserver resourceObserver;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        /**
         * Butterknife初始化
         */
        ButterKnife.bind(this);

        Log.i(TAG, "onCreate: " + time);

        /**
         * retrofit创建对象
         */
        resourceObserver = getApi(KEY, sort, page, pagesize, time);


        /**
         * 刷新操作
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_orange_light
                        , android.R.color.holo_blue_light, android.R.color.holo_green_light);

                /**
                 * 刷新
                 */
                int rePage = page + 1;
                resourceObserver = getApi(KEY, sort, rePage, pagesize, time);
                //应该在compeleted中调用，不然访问不到
//                adapterR.notifyDataSetChanged();

                //隐藏
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * unSubscribe
         */
        unSubscribe(resourceObserver);
        listAll = null;
    }

    private void unSubscribe(ResourceObserver resourceObserver) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(resourceObserver);
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    /**
     * retrofit创建对象
     */
    private ResourceObserver getApi(String KEY, String sort, int page, int pagesize, String time) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://japi.juhe.cn/joke/img/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        IJoke iJoke = retrofit.create(IJoke.class);

//        Observable<Joke> observable = iJoke.getJoke(KEY, sort, page, pagesize, time);

        return   iJoke.getJoke(KEY, sort, page, pagesize, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceObserver<Joke>() {

                    @Override
                    public void onNext(Joke joke) {
                        int errorCode = joke.getErrorCode();
                        String msg = joke.getReason();

                        Result result = joke.getResult();

                        List<Datum> list = result.getData();

                        //添加数据
                        listAll.addAll(list);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Toast.makeText(Second.this, "未知错误！", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        /**
                         * 刷新数据
                         */
                        if (adapterR != null) {
                            adapterR.notifyDataSetChanged();
                        }
                        /**
                         * recycler初始化
                         */
                        if (isFirst) {
                            setUpRecyclerview();
                            isFirst = false;
                        }
                    }
                });
    }

    /**
     * 聚会API接口
     * sort	string	是	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
     * page	int	否	当前页数,默认1
     * pagesize	int	否	每次返回条数,默认1,最大20
     * time	string	是	时间戳（10位），如：1418816972
     */
    interface IJoke {
        @GET("list.from")
        Observable<Joke> getJoke(@Query("key") String key, @Query("sort") String sort, @Query("page") int page
                , @Query("pagesize") int pagesize, @Query("time") String time);
    }

    /**
     * 时间转换
     */
    String timeToString(long l) {
        //秒
        return String.valueOf(l / 1000);
    }

    /**
     * recyclerview 的设置
     */
    private void setUpRecyclerview() {
        if (listAll != null) {
            adapterR = new AdapterR(this, listAll);
            mRecyclerView.setAdapter(adapterR);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }
}
