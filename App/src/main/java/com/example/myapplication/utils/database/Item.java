package com.example.myapplication.utils.database;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/8/3 11:00
 * History:
 * desc: 字段类型封装
 **/
public class Item {
    public static final String item_type_integer = "item_type_integer";
    public static final String item_type_text = "item_type_text";
    public static final String item_type_long = "item_type_long";
    public static final String item_type_boolen = "item_type_boolen";
    public static final String item_type_unique = "item_type_unique";

    public String text = "";//用于SQL拼接的关键字（要是无法理解可见接下来的例子）
    public String type = "";//该item的类型

    public Item(String text,String type)
    {
        this.text = text;
        this.type = type;
    }
    public Item()
    {
    }
}
