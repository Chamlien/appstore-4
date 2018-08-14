package com.example.myapplication.ui.Activity.download;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.DownloadAdapter;
import com.example.myapplication.utils.viewUtils.T;
import com.example.service.OkDownload;
import com.example.service.task.XExecutor;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/7/16 14:23
 * 4 * Desc:下载完成页面，显示所有下载记录
 */

public class DownloadFinishActivity extends AppCompatActivity implements XExecutor.OnAllTaskEndListener{

    private DownloadAdapter adapter;
    private OkDownload okDownload;
    private RecyclerView listview;

    private Button downfinish;

    private LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_finish);
        downfinish=(Button)findViewById(R.id.bt_downfinish);
        back=(LinearLayout)findViewById(R.id.finish_back);
        listview=(RecyclerView)findViewById(R.id.rv_downfinish_list);

        okDownload = OkDownload.getInstance();
        adapter = new DownloadAdapter(this);
        adapter.updateData(DownloadAdapter.TYPE_FINISH);
        listview.setLayoutManager(new LinearLayoutManager(this));
        listview.setAdapter(adapter);

        okDownload.addOnAllTaskEndListener(this);
        initClick();
    }

    private void initClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFinishActivity.this.finish();
            }
        });
        downfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkDownload.getInstance().removeAll();
                adapter.updateData(DownloadAdapter.TYPE_FINISH);
                adapter.notifyDataSetChanged();
                T.show(DownloadFinishActivity.this,"所有下载任务已结束",1);
            }
        });
    }

    @Override
    public void onAllTaskEnd() {
        T.show(this,"所有下载任务已结束",1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okDownload.removeOnAllTaskEndListener(this);
        adapter.unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
