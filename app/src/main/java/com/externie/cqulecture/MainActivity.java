package com.externie.cqulecture;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import engine.Engine;
import model.BannerModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ObservableScrollViewCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.reload();
            }
        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        mOListView = (ObservableListView) findViewById(R.id.list);
//        mOListView.setScrollViewCallbacks(this);
//
//        ArrayList<String> items = new ArrayList<String>();
//        for (int i = 1; i <= 100; i++) {
//            items.add("Item " + i);
//        }

//        mOListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));

        initBannarView();
        initTabLayout();
//        initRefreshView();
        initWebView();
        initPopupView();
        initLoadingView();
    }

    private MaterialRefreshLayout materialRefreshLayout;
    private ObservableListView mOListView;
    private WebView mWebView;
    private WebView mWebView4Profile;
    private PopupWindow popWin;
    private View loadingView;

    private void initLoadingView(){
        loadingView = findViewById(R.id.loadView);
        loadingView.setVisibility(View.INVISIBLE);
    }

    private void showLoadingView(){
        if (loadingView.getVisibility() == View.VISIBLE)
            return;
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingView(){
        loadingView.setVisibility(View.INVISIBLE);
    }

    private void initBannarView(){
        final BGABanner banner = (BGABanner)findViewById(R.id.banner);
        final List<ImageView> imageViews = mallocImageViews(4);
        banner.setViews(imageViews);
        Engine engine = getEngine("http://externie.com/");
        engine.fiveItem().enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                System.out.println("成功");
                BannerModel model = response.body();
                List<String> tips = new ArrayList<String>();
                for (int i = 0; i < model.pages.size(); i++) {
                    Glide.with(MainActivity.this).load(model.pages.get(i).img).placeholder(R.drawable.holdimg).error(R.drawable.holdimg).into(imageViews.get(i));
                    tips.add(model.pages.get(i).profile);
                    // 为每一页添加点击事件
                    final int finalPosition = i;
                    imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(App.getInstance(), "点击了第" + (finalPosition + 1) + "页", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                banner.setTips(tips);
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                System.out.println("失败");
            }
        });
    }

    private Engine getEngine(String url){
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
    }
    private List<ImageView> mallocImageViews(int count){
        List<ImageView> views = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            views.add((ImageView) getLayoutInflater().inflate(R.layout.view_image, null));
        }
        return views;
    }
    private void initTabLayout(){
        TabLayout tabs = (TabLayout)findViewById(R.id.tabLayout);
        tabs.addTab(tabs.newTab().setText(R.string.tab_text_1));
        tabs.addTab(tabs.newTab().setText(R.string.tab_text_2));
        tabs.addTab(tabs.newTab().setText(R.string.tab_text_3));
        tabs.addTab(tabs.newTab().setText(R.string.tab_text_4));
        tabs.addTab(tabs.newTab().setText(R.string.tab_text_5));
    }
    private void initRefreshView(){
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        materialRefreshLayout.setIsOverLay(true);
        materialRefreshLayout.setWaveColor(0xff2f3f9e);
        materialRefreshLayout.setWaveShow(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新...
                mWebView.reload();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //上拉刷新...
            }
        });
    }
    private void initWebView(){
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.loadUrl("http://externie.github.io/externieblog/");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.equals("http://externie.github.io/externieblog/"))
                    view.loadUrl(url);
                else{
                    if (!url.equals(mWebView4Profile.getUrl())){
                        mWebView4Profile.loadUrl(url);
                    }else{
                        changePopupWindowState();
                    }
                }
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    // 结束下拉刷新...
                    if (materialRefreshLayout != null)
                        materialRefreshLayout.finishRefresh();

                } else {
                    // 加载中

                }

            }
        });
        System.out.println("ContentHeight:" + mWebView.getContentHeight());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(mWebView.canGoBack())
            {
                mWebView.goBack();//返回上一页面
            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mWebView.reload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }

    private void initPopupView(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.popupview, null);
        view.setFocusableInTouchMode(true);
        //网页部分
        mWebView4Profile = (WebView)view.findViewById(R.id.webView);
        mWebView4Profile.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        mWebView4Profile.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    hideLoadingView();
                    changePopupWindowState();
                } else {
                    // 加载中
                    showLoadingView();
                }

            }
        });
        WebSettings settings = mWebView4Profile.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //浮动按钮部分
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "你好啊！！！！", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        //Popupwindow部分
        popWin = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popWin.setAnimationStyle(R.style.ExternIEDialogAnimation);
        popWin.setTouchable(true);
        popWin.setOutsideTouchable(true);
        BitmapDrawable bd = new BitmapDrawable();
        popWin.setBackgroundDrawable(bd);

    }

    private void changePopupWindowState() {
        if (popWin.isShowing()) {
            popWin.dismiss();
        }else {
            View text = findViewById(R.id.textforhide);
            popWin.showAtLocation(text, Gravity.BOTTOM, 0, 0);
        }
    }
}
