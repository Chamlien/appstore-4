package com.example.myapplication.ui.Manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.ui.Activity.download.DownloadFinishActivity;
import com.example.myapplication.utils.systemUtils.AppUtil;
import com.example.myapplication.utils.systemUtils.SDCardInfo;
import com.example.myapplication.utils.systemUtils.StorageUtil;
import com.example.myapplication.widget.circleprogress.ArcProgress;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/7/01 09:33
 * 4 * Desc:管理页面
 */

public class ManageFragment extends Fragment {

    ArcProgress arcStore;

    ArcProgress arcProcess;

    TextView capacity;

    Context mContext;

    private Timer timer;
    private Timer timer2;

    private RelativeLayout card1;

    private RelativeLayout card2;

    private RelativeLayout card3;

    private RelativeLayout card4;

    public ManageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        mContext = getActivity();

        arcStore = (ArcProgress)view.findViewById(R.id.arc_store);
        arcProcess = (ArcProgress)view.findViewById(R.id.arc_process);
        capacity =(TextView)view.findViewById(R.id.capacity);
        card1=(RelativeLayout)view.findViewById(R.id.card1);
        card2=(RelativeLayout)view.findViewById(R.id.card2);
        card3=(RelativeLayout)view.findViewById(R.id.card3);
        card4=(RelativeLayout)view.findViewById(R.id.card4);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MemoryCleanActivity.class));
            }
        });//内存加速

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RubbishCleanActivity.class));
            }
        });//垃圾清理

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DownloadFinishActivity.class));
            }
        });//下载管理

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SoftwareManageActivity.class));
            }
        });//软件管理
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }


    /**
     * 初始化读取本地内存信息和存储空间
     */
    private void fillData() {

        // TODO Auto-generated method stub
        timer = null;
        timer2 = null;
        timer = new Timer();
        timer2 = new Timer();

        long l = AppUtil.getAvailMemory(mContext);
        long y = AppUtil.getTotalMemory(mContext);
        final double x = (((y - l) / (double) y) * 100);
        //   arcProcess.setProgress((int) x);

        arcProcess.setProgress(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcProcess.getProgress() >= (int) x) {
                            timer.cancel();
                        } else {
                            arcProcess.setProgress(arcProcess.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);

        SDCardInfo mSDCardInfo = StorageUtil.getSDCardInfo();
        SDCardInfo mSystemInfo = StorageUtil.getSystemSpaceInfo(mContext);

        long nAvailaBlock;
        long TotalBlocks;
        if (mSDCardInfo != null) {
            nAvailaBlock = mSDCardInfo.free + mSystemInfo.free;
            TotalBlocks = mSDCardInfo.total + mSystemInfo.total;
        } else {
            nAvailaBlock = mSystemInfo.free;
            TotalBlocks = mSystemInfo.total;
        }

        final double percentStore = (((TotalBlocks - nAvailaBlock) / (double) TotalBlocks) * 100);

        capacity.setText(StorageUtil.convertStorage(TotalBlocks - nAvailaBlock) + "/" + StorageUtil.convertStorage(TotalBlocks));
        arcStore.setProgress(0);

        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcStore.getProgress() >= (int) percentStore) {
                            timer2.cancel();
                        } else {
                            arcStore.setProgress(arcStore.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer2.cancel();
        super.onDestroy();
    }
}
