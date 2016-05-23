package com.externie.EXViews;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.lang.annotation.Annotation;

/**
 * Created by externIE on 16/5/16.
 */
public class EXWebView extends WebView implements JavascriptInterface {
    public EXWebView(Context context) {
        super(context);
    }

    public EXWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EXWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EXWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }


}
