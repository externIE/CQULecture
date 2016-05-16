package com.externie.cqulecture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


/**
 * Created by externIE on 16/5/12.
 */



public class PostActivity extends AppCompatActivity {



    private String mUrl;
    private WebView mWebView;
    private View loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mUrl = getIntent().getStringExtra("url");
        System.out.println("PostActivityCreated!!!!!!");
        mWebView = (WebView)findViewById(R.id.activity_post_wv);
//        initLoadingView();
        initWebView();

    }

    private void initWebView(){
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                toast(url);
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
//                    hideLoadingView();
                } else {
                    // 加载中
//                    showLoadingView();
                }

            }
        });
//        System.out.println("ContentHeight:" + mWebView.getContentHeight());
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setSaveEnabled(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript


        WebSettings settings = mWebView.getSettings();

        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setLoadsImagesAutomatically(true);//异步加载图片

        CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
        CookieSyncManager.getInstance().sync();
    }

//    private void initLoadingView(){
//        loadingView = findViewById(R.id.loadingView);
//        loadingView.setVisibility(View.INVISIBLE);
//    }
//
//    private void showLoadingView(){
//        if (loadingView.getVisibility() == View.VISIBLE)
//            return;
//        loadingView.setVisibility(View.VISIBLE);
//    }
//
//    private void hideLoadingView(){
//        if (loadingView.getVisibility() == View.INVISIBLE)
//            return;
//        loadingView.setVisibility(View.INVISIBLE);
//    }

    protected void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
