package com.wulei.funnypicture.presenter;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.wulei.funnypicture.model.GsonData;
import com.wulei.funnypicture.model.PicsGson;
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

public class PicsFindItemPresenter implements IfindItemPresenter<PicsGson> {
    /**
     * 聚合数据key,Get访问url参数值
     */
    private int page = 1;
    private int pagesize = 20;
    /**
     * 存储数据
     */
    List<PicsGson> list;

    @Override
    public void getItemDataRefresh(FinishedListener<PicsGson> finishedListener, ErrorListener<PicsGson> errorListener) {
        //retrofit初始化
        Retrofit retrofit = RetrofitUtils.newInstance(ConstantUtils.URL_PICS);
        PicsFindItemPresenter.IPicsRetrofit IPicsRetrofit = RetrofitUtils.getQueryInterface(retrofit, PicsFindItemPresenter.IPicsRetrofit.class);
        //发起请求
        requestApi(finishedListener, IPicsRetrofit,errorListener);
    }

    @Override
    public void getItemDataPull(FinishedListener<PicsGson> finishedListener, ErrorListener<PicsGson> errorListener) {
//retrofit初始化
        Retrofit retrofit = RetrofitUtils.newInstance(ConstantUtils.URL_PICS);
        PicsFindItemPresenter.IPicsRetrofit IPicsRetrofit = RetrofitUtils.getQueryInterface(retrofit, PicsFindItemPresenter.IPicsRetrofit.class);
        //下一页
        page++;
        //发起请求
        requestApi(finishedListener, IPicsRetrofit,errorListener);
    }

    /**
     * 发起网络请求数据
     *
     * @param finishedListener 数据完成的回调
     * @param IPicsRetrofit    内部接口
     */
    private void requestApi(final FinishedListener finishedListener, PicsFindItemPresenter.IPicsRetrofit IPicsRetrofit, final ErrorListener<PicsGson> errorListener) {
        //发起请求
        Subscription subscription = IPicsRetrofit.getPicsGson(page, pagesize, ConstantUtils.KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GsonData<PicsGson>>() {

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
                    public void onNext(GsonData<PicsGson> gsonData) {
                        //数据添加
                        Result<PicsGson> result = gsonData.getResult();
                        List<PicsGson> listGson = result.getData();
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
    interface IPicsRetrofit {
        @GET("text.from")
        Observable<GsonData<PicsGson>> getPicsGson(@Query("page") int page
                , @Query("pagesize") int pagesize, @Query("key") String key);
    }
}
