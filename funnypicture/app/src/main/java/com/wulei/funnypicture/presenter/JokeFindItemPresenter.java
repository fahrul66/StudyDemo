package com.wulei.funnypicture.presenter;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.wulei.funnypicture.model.GsonData;
import com.wulei.funnypicture.model.JokeGson;
import com.wulei.funnypicture.model.Result;
import com.wulei.funnypicture.utils.ConstantUtils;
import com.wulei.funnypicture.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wulei on 2017/2/10.
 */

public class JokeFindItemPresenter implements IfindItemPresenter<JokeGson> {
    private static final String TAG = "JokeFindItemPresenter";
    /**
     * 聚合数据key,Get访问url参数值
     */
    private int page = 1;
    private int pagesize = 20;
    /**
     * 存储数据
     */
    List<JokeGson> list;

    /**
     * 下拉刷新时重新获取数据
     *
     * @param finishedListener
     * @return
     */
    @Override
    public void getItemDataRefresh( final FinishedListener<JokeGson> finishedListener,ErrorListener<JokeGson> errorListener) {
        //retrofit初始化
        Retrofit retrofit = RetrofitUtils.newInstance(ConstantUtils.URL_JOKE);
        IJokeRetrofit iJokeRetrofit = RetrofitUtils.getQueryInterface(retrofit, IJokeRetrofit.class);
        //发起请求
        requestApi( finishedListener, iJokeRetrofit,errorListener);
    }

    /**
     * recyclerView分页加载
     *
     * @param finishedListener
     * @return
     */
    @Override
    public void getItemDataPull( final FinishedListener<JokeGson> finishedListener,ErrorListener<JokeGson> errorListener) {
        //retrofit初始化
        Retrofit retrofit = RetrofitUtils.newInstance(ConstantUtils.URL_JOKE);
        IJokeRetrofit iJokeRetrofit = RetrofitUtils.getQueryInterface(retrofit, IJokeRetrofit.class);
        //下一页
        page++;
        //发起请求
        requestApi(finishedListener, iJokeRetrofit,errorListener);
    }

    /**
     * 发起网络请求数据
     *
     * @param finishedListener 数据完成的回调
     * @param iJokeRetrofit    内部接口
     */
    private void requestApi( final FinishedListener finishedListener, IJokeRetrofit iJokeRetrofit, final ErrorListener<JokeGson> errorListener) {
        //发起请求
      Subscription subscription = iJokeRetrofit.getJokeGson( page, pagesize,ConstantUtils.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GsonData<JokeGson>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        list = null;
                        list = new ArrayList<>();
                    }

                    @Override
                    public void onCompleted() {
                        //完成操作
                        finishedListener.finished(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //错误
                        errorListener.error();
                    }

                    @Override
                    public void onNext(GsonData<JokeGson> gsonData) {
                        //数据添加
                        Result<JokeGson> result= gsonData.getResult();
                        List<JokeGson> listGson = result.getData();
                        list.addAll(listGson);
                    }
                });


    }

    /**
     * 聚合API接口
     * sort	string	是	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
     * page	int	否	当前页数,默认1
     * pagesize	int	否	每次返回条数,默认1,最大20
     * time	string	是	时间戳（10位），如：1418816972
     */
    interface IJokeRetrofit {
        @GET("text.from")
        Observable<GsonData<JokeGson>>getJokeGson(@Query("page") int page
                , @Query("pagesize") int pagesize, @Query("key") String key);
    }
}
