package com.externie.gesture;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.externie.cqulecture.MainActivity;

/**
 * Created by externIE on 16/5/4.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private Context mContext;
    public MyGestureListener(Context context) {
        mContext = context;
    }
    // 滑动，按下后滑动
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        if(Math.abs(distanceX)>Math.abs(distanceY)){
            return true;
        }else{
            return false;
        }
//        return super.onScroll(e1,e2,distanceX,distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        MainActivity ma = (MainActivity)mContext;
        if (Math.abs(velocityX)/Math.abs(velocityY) > 1.2){
            if (velocityX > 20){
                ma.switchLeftTab();
            }else if (velocityX < -20){
                ma.switchRightTab();
            }
        }
        return true;
    }
}
