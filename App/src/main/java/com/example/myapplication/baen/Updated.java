package com.example.myapplication.baen;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.utils.database.Item;
import com.example.myapplication.utils.database.SQLiteManager;
import com.example.myapplication.utils.database.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/7/5 10:03
 * History:
 * desc:已更新应用的版本号
 **/
public class Updated extends Table{
    String appName;//名字
    String size;//软件的大小
    String version ;//软件的版本号
    String packageName;//软件的包名
    String time;
    String icons;

    String[] columns = new String[]{"appName","size","version","packageName","time","icons"};

    public Updated() {
            init();
    }

    public void init() {
        table_name = "updated";
        keyItem = "_id";//默认给予主键为“_id”
        //构建属性
        items.add(new Item("appName", Item.item_type_text));
        items.add(new Item("size", Item.item_type_text));
        items.add(new Item("version", Item.item_type_text));
        items.add(new Item("packageName", Item.item_type_unique));
        items.add(new Item("time", Item.item_type_text));
        items.add(new Item("icons", Item.item_type_text));
    }

    public long insert(ContentValues cv){
        return SQLiteManager.getInstance().db.insertWithOnConflict(table_name,null,cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long deleteOneByItem(String item,String content){
        String[] args = {String.valueOf(content)};
        return SQLiteManager.getInstance().db.delete(table_name,item+" =?",args);
    }

    // 查询所有的联系人信息
    public List<Updated> queryUpdate() {
        List<Updated> mList = null;
        Cursor c = SQLiteManager.getInstance().db.query(table_name,columns, null,null,null,null,null);
        if (c.getCount()>0) {
           mList = new ArrayList<Updated>(c.getCount());
           while (c.moveToNext()) {
               Updated localApp = new Updated();
               localApp.setAppName(c.getString(0));
               localApp.setSize(c.getString(1));
               localApp.setVersion(c.getString(2));
               localApp.setPackageName(c.getString(3));
               localApp.setTime(c.getString(4));
               localApp.setIcons(c.getString(5));
               mList.add(localApp);
           }
           c.close();
       }
        return mList;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

}
