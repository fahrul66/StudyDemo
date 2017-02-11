package com.example.wulei.retrofit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by wulei on 2017/1/8.
 */

public class RetrofitUtils {

    /**
     * 创建retrofit对象
     */
    public static Retrofit create(String url, Converter.Factory factory1, CallAdapter.Factory factory2) {

        return new Retrofit.Builder().baseUrl(url)
                                     .addConverterFactory(factory1)
                                     .addCallAdapterFactory(factory2)
                                     .build();
    }

    /**
     * rxjava调用
     */
    public static void subscribe(Observable observable, Observer observer){
        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(observer);

    }
    public static Disposable subscribe(Observable observable, Consumer consumer){
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

}
