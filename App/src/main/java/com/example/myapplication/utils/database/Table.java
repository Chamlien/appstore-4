package com.example.myapplication.utils.database;

import java.util.ArrayList;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/8/3 10:59
 * History:
 * desc: 数据库创建表封装类
 **/
public class Table {
    public String table_name;

    //table strings 表属性列表
    public ArrayList<Item> items = new ArrayList<>();

    //key 自定主键项 一般默认为“_id”
    public String keyItem;

    //db

    public Table()
    {

    }

    public Table(String table_name,String keyItem)
    {
        this.keyItem = keyItem;
        this.table_name = table_name;
    }

    public Table(String table_name,String keyItem,ArrayList<Item> items)
    {
        this.keyItem = keyItem;
        this.table_name = table_name;
        this.items = items;
    }

    /*
    fun 查改增删 方法的具体实现  这里强烈建议具体方法由子类区实现，因为不同的表所对应的查改增删方法都有不同。
    以后我们在使用的时候，我们可以让自己的表对象继承与Table对象，这样项目中就很好区分各个表对象，也可以很好的管理项目中的表。
     */

    //create table

    //delete table

    //inserte item



}
