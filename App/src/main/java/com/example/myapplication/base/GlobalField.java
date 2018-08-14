package com.example.myapplication.base;

/**
 * 2 * Copyright (C), 2018, 宁波瑞泽西医疗科技有限公司
 * 3 * FileName: GlobalField
 * 4 * Author: dell 许格
 * 5 * Date: 2018/6/19 10:45
 * 6 * Description: ${DESCRIPTION}
 * 7 * History:
 * 8 * <author> <time> <version> <desc>
 * 9 * 作者姓名 修改时间 版本号 描述
 * 10* 全局字段
 */
public enum  GlobalField {
    APPS_FIND ("http://61.175.202.190:9001/appstore/");

    private final Object value;

    GlobalField(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return asString();
    }

    public String asString() {
        final Object obj = value;
        if (obj instanceof String) {
            return (String) obj;
        }
        return "";
    }

}
