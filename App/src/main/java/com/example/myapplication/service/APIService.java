package com.example.myapplication.service;

/**
 * 1 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 2 * FileName: APIService
 * 3 * Author: dell 许格
 * 4 * Date: 2018/6/19 10:02
 * 5 * Desc:网络数据传输接口
 */

import com.example.myapplication.baen.Apps;
import com.example.myapplication.baen.VersionCode;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * API--接口  服务[这里处理的是同一的返回格式 resultCode  resultInfo Data<T> --->这里的data才是返回的结果,T就是泛型 具体返回的been对象或集合]
 */
public interface APIService {

    public static String APP_VIDEOS_URL="http://61.175.202.190:9001/video/";
    public static String APP_PICTURE_URL="http://61.175.202.190:9001/pic/";
    public static String APP_APK_URL="http://61.175.202.190:9001/apk/";
    public static String APP_UPDATE_URL="http://61.175.202.190:9001/update/";
    public static String APP_IMAGE_URL="http://61.175.202.190:9001/image/";

    /**
     * 查询所有应用
     *
     * @param start 从第几个开始
     * @param count 到第几个
     * @param type 类型（1-游戏，2-主题，3-更新)
     * @return
     */
    @GET("App/find")
    Observable<Apps> getApps(@Query("start") int start, @Query("count") int count, @Query("type") int type);

    /**
     * 查询版本
     * @param packageName 对应包名
     */
    @GET("query")
    Observable<VersionCode> getVersion(@Query("id") String packageName , @Query("version") String version);
}
