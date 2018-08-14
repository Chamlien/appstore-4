package com.example.myapplication.base;

import android.support.v4.util.ArrayMap;

import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: RxDisposeManager
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/26 11:02
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10
 */
public class RxDisposeManager {

    private static RxDisposeManager sInstance = null;

    private ArrayMap<Object, Disposable> maps;

    public static RxDisposeManager get() {

        if (sInstance == null) {
            synchronized (RxDisposeManager.class) {
                if (sInstance == null) {
                    sInstance = new RxDisposeManager();
                }
            }
        }
        return sInstance;
    }

    private RxDisposeManager() {
        maps = new ArrayMap<>();
    }


    public void add(Object tag, Disposable disposable) {
        maps.put(tag, disposable);
    }


    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            maps.remove(tag);
        }
    }

    public void removeAll() {
        if (!maps.isEmpty()) {
            maps.clear();
        }
    }


    public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isDisposed()) {
            maps.get(tag).dispose();
            maps.remove(tag);
        }
    }

    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}
