package com.example.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication.service.CoreService;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: BootCompleteReceiver
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/11 10:16
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * desc:开启服务的广播
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, CoreService.class);
        context.startService(i);


    }
}
