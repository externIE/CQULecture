package com.externie.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by externIE on 16/5/15.
 */
public class URLImageView extends ImageView {
    private String mPageUrl;
    public void setWithURL(String url) {
        mPageUrl = url;
    }
    public String getWithURL(){
        return mPageUrl;
    }
    public ImageView getImageView(){
        return (ImageView)this;
    }
    public URLImageView(Context context) {
        super(context);
    }

    public URLImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public URLImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public URLImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
