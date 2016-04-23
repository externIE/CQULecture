package com.externie.popupview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.externie.cqulecture.R;

/**
 * Created by externIE on 16/4/18.
 */
public class PopupView extends LinearLayout {
    private static final float DEFAULT_MAX_RATIO = 0.4f;
    private static final float DEFAULT_MAX_HEIGHT = -1f;
    private static final int durationIn = 500;
    private static final int durationOut = 500;

    private Context mContext;
    private float mMaxHeight = DEFAULT_MAX_HEIGHT;//优先级高
    private float mMaxRatio = DEFAULT_MAX_RATIO;//优先级低

    public PopupView(Context context) {
        this(context,null);
    }
    public PopupView(Context context, AttributeSet attrs){
        this(context, attrs,0);
    }
    public PopupView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PopupView);
        final int count = a.getIndexCount();
        for (int i = 0 ; i < count ; i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.PopupView_maxHeightDimen:
                    mMaxHeight = a.getDimension(attr, DEFAULT_MAX_HEIGHT);
                    break;
                case R.styleable.PopupView_maxHeightRatio:
                    mMaxRatio = a.getFloat(attr,DEFAULT_MAX_RATIO);
                    break;
            }
        }
        a.recycle();
        if (mMaxHeight<0){
            mMaxHeight = mMaxRatio*(float)getScreenHeight(context);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d("wang", "onMeasure heightSize is " + heightSize + " | mMaxHeight is " + mMaxHeight);

        if(heightMode == MeasureSpec.EXACTLY){
            Log.d("wang", "heightMode == View.MeasureSpec.EXACTLY");
            heightSize = heightSize <= mMaxHeight ? heightSize : (int)mMaxHeight;
        }

        if(heightMode == MeasureSpec.UNSPECIFIED){
            Log.d("wang", "heightMode == View.MeasureSpec.UNSPECIFIED");
            heightSize = heightSize <= mMaxHeight ? heightSize : (int)mMaxHeight;
        }
        if(heightMode == MeasureSpec.AT_MOST){
            Log.d("wang", "heightMode == View.MeasureSpec.AT_MOST");
            heightSize = heightSize <= mMaxHeight ? heightSize : (int)mMaxHeight;
        }

        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }
    private static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
    private static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
}
