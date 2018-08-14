package com.example.myapplication.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.utils.ScreenUtils;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: App
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/11 16:32
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10
 */
public class App extends Application {

    public static App sContext;
    public static int sHeight;
    public static int sWidth;
    private static final String TAG = "App";
    public static App APP_CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        sWidth = ScreenUtils.getScreenWidth();
        sHeight = ScreenUtils.getScreenHeight();
        APP_CONTEXT = this;
        super.onCreate();
    }

    public static Context getAppContext() {
        return sContext;
    }
}
