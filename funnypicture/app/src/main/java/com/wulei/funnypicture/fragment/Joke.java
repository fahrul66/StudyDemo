package com.wulei.funnypicture.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wulei.funnypicture.Adapter.JokeRecyclerViewAdapter;
import com.wulei.funnypicture.R;
import com.wulei.funnypicture.model.JokeGson;
import com.wulei.funnypicture.presenter.JokePresenter;
import com.wulei.funnypicture.view.IJokeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wulei on 2017/2/9.
 */

public class Joke extends Fragment implements IJokeView<JokeGson>, SwipeRefreshLayout.OnRefreshListener, JokeRecyclerViewAdapter.FenYeCallBack {
    private static final String TAG = "Joke";
    /**
     * 下拉刷新控件
     */
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * recyclerView控件
     */
    private RecyclerView recyclerView;

    /**
     * 适配器
     */
    private JokeRecyclerViewAdapter jokeAdapter;

    /**
     *业务处理对象
     */
    private JokePresenter jokePresenter = new JokePresenter(this);

    private List<JokeGson> listAll ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout.setOnRefreshListener(this);
        //recyclerView设置
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        listAll = new ArrayList();
        jokeAdapter = new JokeRecyclerViewAdapter(getActivity(),listAll);
        jokeAdapter.setFenYeCallBack(this);
        //设置适配器
        recyclerView.setAdapter(jokeAdapter);
    }


    @Override
    public void showProgress() {
        //显示progress
        swipeRefreshLayout.setRefreshing(true);
        //颜色加载
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_blue_light,
                android.R.color.holo_red_light,android.R.color.holo_orange_light);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void setItemData(List<JokeGson> list) {
//        this.listAll = new ArrayList<>();
//        this.listAll.addAll(list);
        jokeAdapter = new JokeRecyclerViewAdapter(getActivity(),list);
        jokeAdapter.setFenYeCallBack(this);
        //设置适配器
        recyclerView.setAdapter(jokeAdapter);
        jokeAdapter.notifyDataSetChanged();
    }

    /**
     * swipeRefreshLayout的刷新功能
     */
    @Override
    public void onRefresh() {
        showProgress();
        //doing something
        if(swipeRefreshLayout.isRefreshing()){
           //刷新操作
            jokePresenter.getRefreshData();
            hideProgress();
        }
    }

    /**
     * 获取view对象
     */
    @Override
    public View getSnackView() {
        return getView();
    }

    /**
     *返回刷新
     */
    @Override
    public void onResume() {
        super.onResume();
        jokePresenter.onResume();
    }
    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        jokePresenter.onDestory();
    }

    /**
     * 分页加载
     */
    @Override
    public void load() {
        //获得下一页数据
        jokePresenter.getPullData();
    }
}
