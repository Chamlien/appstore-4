package com.example.myapplication.ui.Update.MVP;

import android.content.Context;

import com.example.myapplication.baen.Apps;
import com.example.myapplication.baen.VersionCode;
import com.example.myapplication.base.RxDisposeManager;
import com.example.myapplication.widget.listview.LoadingDialog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/7/6 16:23
 * History:
 * desc:
 **/
public class UpdatePresenter implements UpdateContract.UpdatePresenter {

    private UpdateView views;
    private UpdateContract.UpdateModel model;
    private Context context;

    public UpdatePresenter(UpdateView view , Context context){
        this.context=context;
        this.views=view;
        this.model=new UpdateModel();
    }

    @Override
    public void loadVersion(String packageName,String version) {
        Observable<VersionCode> observable = model.loadVersion(packageName,version);
        observable.subscribe(new Observer<VersionCode>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(VersionCode version) {
                views.returnVersion(version);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }

    @Override
    public void loadApps(int start, int count, int type) {
        Observable<List<Apps.message>> observable = model.loadApps(start,count,type);
        observable.subscribe(new Observer<List<Apps.message>>() {
            @Override
            public void onSubscribe(Disposable d) {
                RxDisposeManager.get().add("AppByType", d);
            }

            @Override
            public void onNext(List<Apps.message> messages) {
                views.returnAppMessage(messages);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
