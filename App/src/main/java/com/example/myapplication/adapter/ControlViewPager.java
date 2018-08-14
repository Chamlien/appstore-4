package com.example.myapplication.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/7/24 15:14
 * History:
 * desc:
 **/
public class ControlViewPager extends ViewPager {

    private boolean isCanScroll = true;

    public ControlViewPager(Context context) {
        super(context);
    }

    public ControlViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

    }



    public void setScanScroll(boolean isCanScroll){

        this.isCanScroll = isCanScroll;

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return  false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}

