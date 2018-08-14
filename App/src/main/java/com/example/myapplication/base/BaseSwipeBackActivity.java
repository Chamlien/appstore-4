package com.example.myapplication.base;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.utils.viewUtils.MyUtils;
import com.example.myapplication.widget.swipeback.SwipeBackActivityBase;
import com.example.myapplication.widget.swipeback.SwipeBackActivityHelper;
import com.example.myapplication.widget.swipeback.SwipeBackLayout;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: BaseSwipeBackActivity
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/7 11:18
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10
 */
public abstract class BaseSwipeBackActivity extends BaseBackActivity implements SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        MyUtils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}

