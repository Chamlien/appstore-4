package com.example.myapplication.ui.Apps.MVP;

import android.content.Context;

import com.example.myapplication.baen.Apps;
import com.example.myapplication.base.RxDisposeManager;
import com.example.myapplication.utils.viewUtils.T;
import com.example.myapplication.widget.listview.LoadingDialog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: AppPresenter
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/19 14:37
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10
 */
public class AppPresenter implements AppsContract.AppsPresenter {
    private AppView views;
    private AppsContract.AppsModel model;
    private Context context;

    public AppPresenter(AppView view , Context context){
        this.context=context;
        this.views=view;
        this.model=new AppModel();
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
                LoadingDialog.cancelDialogForLoading();
                T.show(context,"请检查网络",1);
                views.dismiss();
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
