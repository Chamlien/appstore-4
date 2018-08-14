package com.example.myapplication.ui.Activity.other;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.library.LoadingView.AnimatedCircleLoadingView;
import com.example.myapplication.R;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/8/01 08:53
 * 4 * Desc:管理页面缓冲刷新
 */
public class ScanningActivity extends AppCompatActivity{

    AnimatedCircleLoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        loadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view2);
        loadingView.startDeterminate();
        startPercentMockThread();
    }

    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    for (int i = 0; i <= 100; i=i+2) {
                        Thread.sleep(50);
                        changePercent(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingView.setPercent(percent);
                if (percent==100){
                    finish();
                }
            }
        });
    }

}
