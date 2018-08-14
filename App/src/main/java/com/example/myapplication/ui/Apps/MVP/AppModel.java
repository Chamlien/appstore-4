package com.example.myapplication.ui.Apps.MVP;

import com.example.myapplication.baen.Apps;
import com.example.myapplication.service.APIService;
import com.example.myapplication.receiver.RetrofitClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;


/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: AppModel
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/19 10:32
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10
 */
public class AppModel implements AppsContract.AppsModel {

    @Override
    public Observable<List<Apps.message>> loadApps(int start, int count, int type) {

        APIService apiService = RetrofitClient.create(APIService.class);

        return apiService.getApps(start,count,type).map(new Function<Apps, List<Apps.message>>() {
            @Override
            public List<Apps.message> apply(Apps apps) throws Exception {
                return apps.getMessage();
            }
        }).compose(RetrofitClient.schedulersTransformer);
    }
}
