package com.example.myapplication.ui.Update.MVP;

import com.example.myapplication.baen.Apps;
import com.example.myapplication.baen.VersionCode;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/7/6 16:00
 * History:
 * desc:这里主要是抽出MVP中三层的接口,好处就是改需求很方便,使得代码结构更加清晰
 **/
public class UpdateContract {

    /**
     * M逻辑(业务)处理层
     */
    interface UpdateModel {

        Observable<VersionCode> loadVersion (@Query("id") String packageName,@Query("version") String version);
        Observable<List<Apps.message>> loadApps (@Query("start") int start, @Query("count") int count, @Query("type") int type );
    }


    /**
     * P视图与逻辑处理的连接层
     */
    interface UpdatePresenter{

        void  loadVersion (String packageName,String version); //获取数据

        void  loadApps (int start, int count, int type ); //获取数据

    }
}
