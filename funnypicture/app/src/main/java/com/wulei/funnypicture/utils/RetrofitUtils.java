package com.wulei.funnypicture.utils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wulei on 2017/2/10.
 */

public class RetrofitUtils {
    /**
     * retrofit的封装,创建
     */
    public static Retrofit newInstance(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * retrofit获取查询接口,静态泛型方法，泛型编译的时候类型擦除
     */
    public static <T> T getQueryInterface(Retrofit retrofit, Class<T> queryInterface) {
        T t = retrofit.create(queryInterface);
        return t;
    }

}
