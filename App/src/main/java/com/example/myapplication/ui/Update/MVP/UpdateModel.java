package com.example.myapplication.ui.Update.MVP;

import com.example.myapplication.baen.Apps;
import com.example.myapplication.baen.VersionCode;
import com.example.myapplication.receiver.RetrofitClient;
import com.example.myapplication.service.APIService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/7/6 16:02
 * History:
 * desc:
 **/
public class UpdateModel implements UpdateContract.UpdateModel {

    APIService apiService = RetrofitClient.create(APIService.class);
    @Override
    public Observable<VersionCode> loadVersion(String packageName,String version) {

        return apiService.getVersion(packageName,version).compose(RetrofitClient.schedulersTransformer);
    }

    @Override
    public Observable<List<Apps.message>> loadApps(int start, int count, int type) {
        return apiService.getApps(start,count,type).map(new Function<Apps, List<Apps.message>>() {
            @Override
            public List<Apps.message> apply(Apps apps) throws Exception {
                return apps.getMessage();
            }
        }).compose(RetrofitClient.schedulersTransformer);
    }
}
