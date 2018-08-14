package com.example.myapplication.baen;

import android.graphics.drawable.Drawable;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: CacheListItem
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/11 9:58
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * desc：清理垃圾显示软件信息实体类
 */
public class CacheListItem {

    private long mCacheSize;
    private String mPackageName, mApplicationName;
    private Drawable mIcon;

    public CacheListItem(String packageName, String applicationName, Drawable icon, long cacheSize) {
        mCacheSize = cacheSize;
        mPackageName = packageName;
        mApplicationName = applicationName;
        mIcon = icon;
    }

    public Drawable getApplicationIcon() {
        return mIcon;
    }

    public String getApplicationName() {
        return mApplicationName;
    }

    public long getCacheSize() {
        return mCacheSize;
    }

    public String getPackageName() {
        return mPackageName;
    }
}