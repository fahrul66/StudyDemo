package com.wulei.runner.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wulei.runner.R;
import com.wulei.runner.activity.MainActivity;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.ToastUtil;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/01.
 */

public class FragmentNews extends BaseFragment {

    @BindView(R.id.coordinate_news)
    CoordinatorLayout mCoordiante;
    @BindView(R.id.webView)
    WebView mWebView;
    private WebSettings mWebSetting;
    @BindView(R.id.pro_news)
    ProgressBar mPro;

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mPro.setMax(100);
        mPro.setProgress(0);
        mPro.setVisibility(View.GONE);
        mWebSetting = mWebView.getSettings();
        mWebSetting.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        mWebSetting.setUseWideViewPort(true); //将图片调整到适合webview的大小
        mWebSetting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        mWebSetting.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        mWebSetting.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSetting.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //设置使用缓存
        mWebSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    @Override
    protected void setListener() {
        mWebView.loadUrl(ConstantFactory.URL);
       mWebView.setWebViewClient(new WebViewClient(){
           @TargetApi(Build.VERSION_CODES.LOLLIPOP)
           @Override
           public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
               view.loadUrl(request.getUrl().getPath());
               return true;
           }

           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
               view.loadUrl(url);
               return true;
           }


       });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    // 网页加载完成
                    mPro.setVisibility(View.GONE);
                } else {
                    // 加载中
                    mPro.setVisibility(View.VISIBLE);
                    mPro.setProgress(newProgress);
                }
            }
        });

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        //清除网页缓存
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
         * 获得焦点，处理back事件
         */
        mWebView.setFocusableInTouchMode(true);
        mWebView.requestFocus();
        mWebView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ( keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        // handle back button's click listener
                        mWebView.goBack();
                        return true;
                    }
                    return false;
                }
            });
    }
}
