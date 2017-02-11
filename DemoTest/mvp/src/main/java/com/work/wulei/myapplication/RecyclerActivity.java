package com.work.wulei.myapplication;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.work.wulei.Adapter.RecyclerActivityAdapter;
import com.work.wulei.Presenter.IRecyclerPresenter;
import com.work.wulei.Presenter.RecyclerPresenter;

import java.util.List;

public class RecyclerActivity extends AppCompatActivity implements IRecyclerView, RecyclerActivityAdapter.onItemSelectListener {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    //view中只能持有presenter的引用
    private IRecyclerPresenter mIRecyclerPresenter;
    private RecyclerActivityAdapter mRecyclerActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        //init
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mIRecyclerPresenter = new RecyclerPresenter(this);

        //recycler
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    protected void onResume() {
        super.onResume();
        mIRecyclerPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIRecyclerPresenter.onDestory();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setItemData(List<String> list) {
        //数据传递，从model到presenter，传给view后，在Activity中实现
        mRecyclerActivityAdapter = new RecyclerActivityAdapter(this,list);
        mRecyclerActivityAdapter.setOnItemSelectListener(this);
        mRecyclerView.setAdapter(mRecyclerActivityAdapter);
    }

    @Override
    public void showItemMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        mIRecyclerPresenter.onItemClick(position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }
}
