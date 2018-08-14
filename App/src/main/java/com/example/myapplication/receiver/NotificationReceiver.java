package com.example.myapplication.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.service.UpdateService;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: dell 许格
 * 3 * Date: 2018/8/11 10:22
 * 4 * desc:判断有更新就开启通知栏提示
 */

public class NotificationReceiver extends BroadcastReceiver{


    private static boolean isClick = false;

    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        isClick = intent.getBooleanExtra("isClick",false);
        mContext=context;
        if (UpdateService.isUpdate==true){
            promptUpdate(mContext);
        }
        promptUpdate(context);
    }
    /**
     *  通知栏提示更新
     */
    private void promptUpdate(Context context){
        Intent broadcastIntent = new Intent(context, NotificationIntentReceiver.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("康复应用商店发现新版本")
                .setContentText("前往商店更新")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.store);

        NotificationManager manager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
    }

}
