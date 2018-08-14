package com.example.myapplication.ui.Apps.MVP;

import com.example.myapplication.baen.Apps;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: AppsContract
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/19 10:22
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10 具体的逻辑(业务)处理了
 * 这里主要是抽出MVP中三层的接口,好处就是改需求很方便,使得代码结构更加清晰
 */

public class AppsContract {

    /**
     * M逻辑(业务)处理层
     */
    interface AppsModel {

        Observable<List<Apps.message>> loadApps (@Query("start") int start, @Query("count") int count, @Query("type") int type );
    }


    /**
     * P视图与逻辑处理的连接层
     */
    interface AppsPresenter{

        void  loadApps (int start, int count, int type ); //获取数据

    }

}
