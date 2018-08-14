package com.example.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication.ui.Activity.other.MainActivity;
import com.example.myapplication.utils.systemUtils.AppUtil;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: dell 许格
 * 3 * Date: 2018/8/11 10:16
 * 4 * desc:启动通知栏的消息
 */

public class NotificationIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        if(AppUtil.isAppRunning(context, "com.example.store")==true){

            Intent mainIntent = new Intent(context, MainActivity.class);

            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(mainIntent);
        }else {
           Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.example.store");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(launchIntent);
        } }

}
