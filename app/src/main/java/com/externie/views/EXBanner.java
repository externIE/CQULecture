package com.externie.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;


import com.nineoldandroids.view.ViewHelper;

import java.util.List;
import java.util.ResourceBundle;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by externIE on 16/5/14.
 */
public class EXBanner extends BGABanner implements ViewPager.OnPageChangeListener{
    private List<String> mTips;
    private String currentTip;
    public EXBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(this);
        currentTip = "CQU学术讲座";
    }
    public EXBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnPageChangeListener(this);
        currentTip = "CQU学术讲座";
    }


    @Override
    public void setTips(List<String>  tips){
        super.setTips(tips);
        mTips = tips;
        currentTip = tips.get(0);
    }


    public String getCurrentTip(){
        return currentTip;
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mTips == null)
                return;
            if (positionOffset > 0.5) {
                currentTip = mTips.get((position + 1) % mTips.size());
            } else {
                currentTip = mTips.get(position % mTips.size());
            }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
