package com.example.myapplication.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.IBinder;

import com.example.myapplication.baen.Apps;
import com.example.myapplication.baen.LocalApp;
import com.example.myapplication.baen.VersionCode;
import com.example.myapplication.ui.Update.MVP.UpdatePresenter;
import com.example.myapplication.ui.Update.MVP.UpdateView;
import com.example.myapplication.utils.database.SQLiteManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * Author: 许格
 * 3 * Date: 2018/8/06 09:23
 * 4 * Desc:检查更新服务（与服务器对比，版本号低于服务器版本，显示更新）
 */

public class UpdateService extends Service implements UpdateView {

    private UpdatePresenter presenter;
    private List<LocalApp> mList = new ArrayList<>();
    private List<ResolveInfo> resolveInfoList;
    private Context context;
    private LocalApp localApp;
    public static boolean isUpdate = false;

    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        presenter = new UpdatePresenter(this,context);
        new UpdateAsyncTask().execute();
        initSql(context);
    }

    private void initSql(Context context) {
        localApp = new LocalApp();
        //init SQL
        SQLiteManager.getInstance().init(context,"updateApp");
        SQLiteManager.getInstance().registerTable(localApp);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

    // 创建一个内部类来实现 ,在实现下面内部类之前,需要自定义的Bean对象来封装处理Josn格式的数据
    class  UpdateAsyncTask extends AsyncTask<String,Void,List<LocalApp>> {
         @Override
         protected List<LocalApp> doInBackground(String... strings) {
             return downloadUpdateFile(context);
         }
    }

    //获取本地所有应用
    private List<LocalApp> downloadUpdateFile(Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);

        for (int i = 0; i < resolveInfoList.size(); i++) {
            LocalApp bean = new LocalApp();
            try {
                bean.setAppName(resolveInfoList.get(i).loadLabel(context.getPackageManager()).toString());
                String packageName = resolveInfoList.get(i).activityInfo.packageName;
                bean.setPackageName(resolveInfoList.get(i).activityInfo.packageName);
                bean.setIcon(resolveInfoList.get(i).loadIcon(context.getPackageManager()));
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                String versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
                bean.setVersion(versionCode);
                mList.add(bean);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    //判断是否需要进行更新
                    isNeedUpdate(bean,i);
                } else {
                    //系统应用（不判断）
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }

    private void isNeedUpdate(LocalApp bean, int i) {
        presenter.loadVersion(bean.getPackageName(),bean.getVersion());
    }

    //需要更新版本的应用保存到本地
    @Override
    public void returnVersion(VersionCode version) {
        if (version.getResult()==1) {
            SQLiteManager.getInstance().open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("appName",version.getName());
            contentValues.put("packageName",version.getPackagename());
            contentValues.put("version",version.getVersion());
            contentValues.put("function",version.getFunction());
            contentValues.put("time",version.getTime());
            contentValues.put("size",version.getSize());
            contentValues.put("url",version.getUrl());
            contentValues.put("icons",version.getIcon());
            localApp.insert(contentValues);
            SQLiteManager.getInstance().close();
            isUpdate=true;
        }
    }

    @Override
    public void returnAppMessage(List<Apps.message> messages) {

    }
}
