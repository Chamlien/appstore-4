package com.example.myapplication.baen;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/7/6 15:46
 * History:
 * desc:获取更新应用的json数据
 **/
public class VersionCode {
    private int result;//网络查询更新返回结果
    private String name;//应用名称
    private String packagename;//包名
    private String version;//版本号
    private String time;//更新时间
    private String size;//大小
    private String url;//链接
    private String function;//新版本功能
    private String icon;//图标

    public String getPackagename() {
        return packagename;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
