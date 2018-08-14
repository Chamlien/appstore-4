package com.example.myapplication.ui.Activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.example.library.FlashTextView.SlackLoadingView;
import com.example.myapplication.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/8/01 08:53
 * 4 * Desc:欢迎页面，欢迎动画
 */

public class WelcomeActivity extends AppCompatActivity{

    SlackLoadingView slackLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        slackLoadingView = (SlackLoadingView) findViewById(R.id.loading_view);

        slackLoadingView.start();

        setup();

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent1=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent1);
                WelcomeActivity.this.finish();
            }
        };
        timer.schedule(timerTask,1000*4);
    }

    /**
     * 创建一个apk下载文件夹
     */

    private void setup() {
        //新建一个File，传入文件夹目录
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ApkDownload/");
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建<span style="color:#FF0000;">目录中包含却不存在</span>的文件夹
            file.mkdirs();
        }
    }

}
