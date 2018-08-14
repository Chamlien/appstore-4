package com.example.myapplication.ui.Apps;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.service.APIService;
import com.example.myapplication.utils.helper.ImageLoaderUtils;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/8/01 08:53
 * 4 * Desc:应用详情列表
 */

public class AppsActivity extends BaseActivity {

    TextView name;
    TextView function;
    TextView version;
    TextView developers;
    TextView difficulty;
    TextView size;
    TextView label;
    TextView history;
    TextView time;
    TextView type;
    ImageView icon;
    VideoView play;
    LinearLayout back;
    ImageView iv_pic1;
    ImageView iv_pic2;
    ImageView iv_pic3;

    String app_name;
    String app_function;
    String app_version;
    String app_developers;
    String app_difficulty;
    double app_size;
    String app_label;
    int app_type;
    String app_urls;
    String app_icon;
    String app_video;
    int app_history;
    String app_image1;
    String app_image2;
    String app_image3;
    String app_time;


    @Override
    public int getLayoutId() {
        return R.layout.activity_apps;
    }

    @Override
    public void initPresenter() {
        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if (extras!=null){//获取数据
            app_name=(String) extras.get("name");
            app_icon=(String) extras.get("icon");
            app_function=(String) extras.get("function");
            app_version=(String) extras.get("version");
            app_developers=(String) extras.get("developers");
            app_difficulty=(String) extras.get("difficulty");
            app_size=(double) extras.get("size");
            app_urls=(String) extras.get("urls");
            app_video=(int) extras.get("video")+".mp4";
            app_history=(int) extras.get("history");
            app_history=(int) extras.get("type");
            app_label=(String) extras.get("label");
            app_time=(String) extras.get("time");
            app_image1=APIService.APP_IMAGE_URL+(String) extras.get("image1");
            app_image2=APIService.APP_IMAGE_URL+(String) extras.get("image2");
            app_image3=APIService.APP_IMAGE_URL+(String) extras.get("image3");
        }
    }

    @Override
    public void initView() {
        name = (TextView) findViewById(R.id.apps_details_name);
        function = (TextView) findViewById(R.id.apps_details_newfunction);
        version = (TextView) findViewById(R.id.apps_details_version);
        label = (TextView) findViewById(R.id.apps_details_label);
        developers = (TextView) findViewById(R.id.apps_details_developers);
        difficulty = (TextView) findViewById(R.id.apps_details_difficulty);
        size = (TextView) findViewById(R.id.apps_details_size);
        icon = (ImageView) findViewById(R.id.apps_details_icon);
        iv_pic1 = (ImageView) findViewById(R.id.iv_apps_pic1);
        iv_pic2 = (ImageView) findViewById(R.id.iv_apps_pic2);
        iv_pic3 = (ImageView) findViewById(R.id.iv_apps_pic3);
        play = (VideoView) findViewById(R.id.videoView);
        back = (LinearLayout) findViewById(R.id.apps_details_back);
        time = (TextView) findViewById(R.id.apps_details_time);

        play.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppsActivity.this.finish();
            }
        });


        initDate();
    }

    private void initDate() {
        name.setText(app_name);
        ImageLoaderUtils.display(this,icon,app_icon);
        ImageLoaderUtils.display(this,iv_pic1,app_image1);
        ImageLoaderUtils.display(this,iv_pic2,app_image2);
        ImageLoaderUtils.display(this,iv_pic3,app_image3);
        function.setText(""+app_function);
        version.setText("版本："+app_version);
        developers.setText(app_developers);
        difficulty.setText(app_difficulty);
        size.setText(app_size+"MB");
        label.setText(app_label);
        time.setText(app_time);

        if (app_type==1){
            type.setText("游戏");
        }else if (app_type==2){
            type.setText("主题");
        }

        //播放app介绍视频
        /*Uri uri = Uri.parse(APIService.APP_VIDEOS_URL+app_video);
        play.setMediaController(new MediaController(this));//显示控制栏
        play.setOnCompletionListener( new MyPlayerOnCompletionListener());
        play.setVideoURI(uri);
        play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                play.start();
            }
        });*/
    }
    /*class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( AppsActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
*/


}
