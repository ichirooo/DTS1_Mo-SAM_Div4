package com.salamander.mo_sam_div4_dts1.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    private boolean swipeable;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeable = true;
    }

    public CustomViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (swipeable) {
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (swipeable) {
            return super.onInterceptTouchEvent(event);
        } else {
            return false;
        }
    }

    public boolean setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
        return swipeable;
    }
}
