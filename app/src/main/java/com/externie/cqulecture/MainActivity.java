package com.externie.cqulecture;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.externie.EXAdapter.EXNavListAdapter;
import com.externie.EXHelper.EXReminderHelper;
import com.externie.EXHelper.EXSQLiteHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import engine.Engine;

import com.externie.EXViews.*;

import model.BannerModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ObservableScrollViewCallbacks, JavascriptInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mTb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTb);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);

        initBannarView();
        initTabLayout();
        initWebView();
        initPopupView();
        initLoadingView();
        initNestedScrollView();
        initNavView();
    }

    private void initNavView() {
        ListView lv = (ListView)findViewById(R.id.left_colletion);
        LayoutInflater inflater = LayoutInflater.from(this);
        lv.addHeaderView(inflater.inflate(R.layout.nav_header_main,lv,false));
        exNavListAdapter = new EXNavListAdapter(this);
        lv.setAdapter(exNavListAdapter);
        EXSQLiteHelper.dumpData2Adapter(this,exNavListAdapter);
    }

    private EXNavListAdapter exNavListAdapter;
    private CollapsingToolbarLayout mToolbarLayout;
    private Toolbar mTb;
    private MaterialRefreshLayout materialRefreshLayout;
    private WebView mWebView;
    private WebView mWebView4Profile;
    private PopupWindow popWin;
    private View loadingView;
    private NestedScrollView mNestedScrollView;
    private GestureDetector mGestureDetector;
    private TabLayout mTabs;
    private Menu mMenu;

    private void initNestedScrollView() {
        mNestedScrollView = (NestedScrollView) findViewById(R.id.scrollView_content);
//        mGestureDetector = new GestureDetector(this,new EXGestureListener(this));
//        mNestedScrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return mGestureDetector.onTouchEvent(event);
//            }
//        });
    }

    public void switchLeftTab() {
        if (mTabs.getSelectedTabPosition() == 0) {
            Toast.makeText(MainActivity.this, "到最左边了！", Toast.LENGTH_SHORT).show();
            return;
        }
        int leftTabPosition = (mTabs.getSelectedTabPosition() - 1) % mTabs.getTabCount();
//        System.out.println("tabPosition:"+leftTabPosition);
        mTabs.getTabAt(leftTabPosition).select();
    }

    public void switchRightTab() {
        if (mTabs.getSelectedTabPosition() == mTabs.getTabCount() - 1) {
            Toast.makeText(MainActivity.this, "到最右边了！", Toast.LENGTH_SHORT).show();
            return;
        }
        int rightTabPosition = (mTabs.getSelectedTabPosition() + 1) % mTabs.getTabCount();
//        System.out.println("tabPosition:"+rightTabPosition);
        mTabs.getTabAt(rightTabPosition).select();
    }

    private void initLoadingView() {
        loadingView = findViewById(R.id.loadView);
        loadingView.setVisibility(View.INVISIBLE);
    }

    private void showLoadingView() {
        if (loadingView.getVisibility() == View.VISIBLE)
            return;
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingView() {
        loadingView.setVisibility(View.INVISIBLE);
    }

    private void initBannarView() {
        final EXBanner banner = (EXBanner) findViewById(R.id.banner);
        final List<URLImageView> imageViews = mallocImageViews(4);
        banner.setViews(imageViews);
        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                banner.getChildAt(position);
//                Log.d("TitleSet:",banner.getCurrentTip());
                String tmp_tip = banner.getCurrentTip();
                mToolbarLayout.setTitle(tmp_tip);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Engine engine = getEngine("http://externie.com/hotlectureinfo/");
        engine.fourItem().enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                final BannerModel model = response.body();
                List<String> tips = new ArrayList<String>();
                for (int i = 0; i < model.pages.size(); i++) {
                    Glide.with(MainActivity.this).load(model.pages.get(i).img).placeholder(R.drawable.holdimg).error(R.drawable.holdimg).into(imageViews.get(i).getImageView());
                    tips.add(model.pages.get(i).profile);
                    imageViews.get(i).setWithURL(model.pages.get(i).pageurl);
                    imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            URLImageView urliv = (URLImageView) v;
                            jumpToPostActivity(urliv.getWithURL());
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

    private Engine getEngine(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
    }

    private List<URLImageView> mallocImageViews(int count) {
        List<URLImageView> views = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            views.add((URLImageView) getLayoutInflater().inflate(R.layout.view_image, null));
        }
        return views;
    }

    private void initTabLayout() {
        mTabs = (TabLayout) findViewById(R.id.tabLayout);
        mTabs.addTab(mTabs.newTab().setText(R.string.tab_text_1));
        mTabs.addTab(mTabs.newTab().setText(R.string.tab_text_2));
        mTabs.addTab(mTabs.newTab().setText(R.string.tab_text_3));
        mTabs.addTab(mTabs.newTab().setText(R.string.tab_text_4));
        mTabs.addTab(mTabs.newTab().setText(R.string.tab_text_5));
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private TabLayout.Tab lastTab = null;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String from = "right";
                if (lastTab != null && lastTab.getPosition() > tab.getPosition())
                    from = "left";

                String jsfunc;
                String campus = "ALL";

                if (tab.getText().toString().equals(getString(R.string.tab_text_1))) {
                    campus = "ALL";
                }
                if (tab.getText().toString().equals(getString(R.string.tab_text_2))) {
                    campus = "A";
                }
                if (tab.getText().toString().equals(getString(R.string.tab_text_3))) {
                    campus = "B";
                }
                if (tab.getText().toString().equals(getString(R.string.tab_text_4))) {
                    campus = "C";
                }
                if (tab.getText().toString().equals(getString(R.string.tab_text_5))) {
                    campus = "D";
                }

                jsfunc = "javascript:select('" + campus + "','" + from + "')";

                mWebView.loadUrl(jsfunc);

                mNestedScrollView.smoothScrollTo(0, 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                lastTab = tab;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initRefreshView() {
//        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
//        materialRefreshLayout.setIsOverLay(true);
//        materialRefreshLayout.setWaveColor(0xff2f3f9e);
//        materialRefreshLayout.setWaveShow(true);
//        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
//            @Override
//            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
//                //下拉刷新...
//                mWebView.reload();
//            }
//
//            @Override
//            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
//                //上拉刷新...
//            }
//        });
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.loadUrl("http://externie.github.io/externieblog/");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.equals("http://externie.github.io/externieblog/"))
                    view.loadUrl(url);
                else {
//                    if (!url.equals(mWebView4Profile.getUrl())){
//                        mWebView4Profile.loadUrl(url);
//                    }else{
//                        changePopupWindowState();
//                    }
                    jumpToPostActivity(url);
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

        mWebView.addJavascriptInterface(this, "android");

        System.out.println("ContentHeight:" + mWebView.getContentHeight());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setLoadsImagesAutomatically(true);//异步加载图片
    }

    private void jumpToPostActivity(String url) {
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
//        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
//        testItem = menu.add("测试");
        return true;
    }

    private MenuItem testItem;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String title = item.getTitle().toString();
        for (int i = 0; i < mMenu.size(); i++) {
            if (title.equals(mMenu.getItem(i).getTitle().toString())) {
                String jsFunc = "javascript:selectCategory('" + mMenu.getItem(i).getTitle() + "')";
                mWebView.loadUrl(jsFunc);
            }
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

    private void initPopupView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.popupview, null);
        view.setFocusableInTouchMode(true);
        //网页部分
        mWebView4Profile = (WebView) view.findViewById(R.id.webView);
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
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
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
        } else {
            View text = findViewById(R.id.textforhide);
            popWin.showAtLocation(text, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @JavascriptInterface
    public void setCategoryItem(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在里对Android应用的UI进行处理
                mMenu.add(str);
            }
        });
    }

    @JavascriptInterface
    public void collectLecture(final String url, final String title, final String date, final String time, final String address) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //这里对获得的收藏讲座信息进行存储处理
                if (!EXSQLiteHelper.existedData(MainActivity.this, title)) {
                    EXSQLiteHelper.storeData(MainActivity.this, url, title, date, time, address);
                    exNavListAdapter.addItem(url, title, date, time, address);

                }
            }
        });
    }

}
