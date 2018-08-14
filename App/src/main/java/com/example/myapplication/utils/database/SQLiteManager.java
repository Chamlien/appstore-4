package com.example.myapplication.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Copyright (C), 宁波瑞泽西医疗科技有限公司
 * Author: dell 许格（软件部）
 * Date: 2018/8/3 10:57
 * History:
 * desc:数据库帮助类
 **/
public class SQLiteManager {
    public SQLiteDatabase db;//数据库操作对象
    private Context context; //上下文
    private DBOpenHelper dbOpenHelper;//建表所需的帮助类
    public class DBOpenHelper extends SQLiteOpenHelper {
        public DBOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            int size = tables.size();//获取当前需要建表的数量
            for(int i = 0;i<size;i++)//拼接建表SQL语句
            {
                String sql = getTableCreateSQLString(tables.get(i));
                _db.execSQL(sql);//执行建库建表语句
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion,int _newVersion) {
            int size = tables.size();//同创建表机制，拼接跟新表SQL语句
            for(int i = 0;i<size;i++)
            {
                _db.execSQL("DROP TABLE IF EXISTS " + getTableCreateSQLString(tables.get(i)));
            }
            onCreate(_db);
        }// end for private static class DBOpenHelper extends SQLiteOpenHelper
    }

    // DB_name
    private String DB_NAME;

    // DB_version
    private int DB_VERSION = 1;

    //ArrayList <Table>
    private ArrayList<Table> tables = new ArrayList<>();//该库中表对象列表

    /*
    fun
     */
    //constrat
    public SQLiteManager()
    {

    }

    //init 初始化建库工作，传入库名
    public void init(Context context,String DB_name)
    {
        tables.clear();
        this.context = context;
        this.DB_NAME = DB_name;
    }

    private static SQLiteManager instance = null;

    public static SQLiteManager getInstance()//单例模式 方便外部调用
    {
        if(instance == null)
        {
            synchronized (SQLiteManager.class)
            {
                if(instance == null)
                {
                    instance = new SQLiteManager();
                }
            }
        }
        return instance;
    }


    //open 做数据库操作之前都要调用open方法
    public void open() {

        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            exceptionHandler();
        }
        db.beginTransaction();
    }

    //read 读取数据库之前都要调用read方法
    public void read() {

        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getReadableDatabase();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            exceptionHandler();
        }
    }

    /**
     * 数据库文件损坏删除异常处理
     *
     */
    private void exceptionHandler() {
        if(db == null)
        {
            return;
        }
        File file = new File(db.getPath());
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    open();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //close 长时间不适用数据库可执行close方法关闭数据库
    public void close() {

        db.setTransactionSuccessful();
        db.endTransaction();
        if (db != null) {
            db.close();
            db = null;

        }
    }




    //create table 注册表 外界任何想在该库下创建的表都必须注册 注册后tables列表会增加，这回作用到建表时SQL语句的生成
    public void registerTable(String table_name, ArrayList<Item> items)
    {
        Table table = new Table(table_name,"_id",items);

        tables.add(table);
    }
    //两种不同的注册方式 建议使用第二种方式进行注册，因为第二种方式的表对象是外界传入的，外界可以利用该对象进行更多的操作。详情见下
    public void registerTable(Table table)
    {
        tables.add(table);
    }

    // get table create sql by items 建表语句拼接代码 遍历tables列表，由于每个表又可以有多个字段，故字段类型不同又有对应的语句区别。
    private static String getTableCreateSQLString(Table table) {
        String sql = "create table "+table.table_name;//表名 下面依次是“主键”+“item1”+“item2”+.....+"item n" 拼接代码原理不熟的百度能搜出一大把，这里只是稍微换了一种形式的写法。

        sql += "("+table.keyItem+" integer primary key autoincrement";

        int size = table.items.size();

        for(int i = 0;i<size;i++)
        {
            sql += ",";
            if(table.items.get(i).type.equals(Item.item_type_integer))
            {
                sql += table.items.get(i).text+" integer not null";
            }else if(table.items.get(i).type.equals(Item.item_type_text))
            {
                sql += table.items.get(i).text+" text not null";
            }else if(table.items.get(i).type.equals(Item.item_type_boolen))
            {
                sql += table.items.get(i).text+" bool not null";
            }else if(table.items.get(i).type.equals(Item.item_type_long))
            {
                sql += table.items.get(i).text+" long not null";
            }else if(table.items.get(i).type.equals(Item.item_type_unique))
            {
                sql += table.items.get(i).text+" text unique not null";
            }
            // TODO: 16-8-23 add other data type 可能有别的数据属性 要看SQLite还支持那些数据的存储



        }
        sql += ");";
        Log.i("coyc","getTableCreateSQLString"+sql);
        return sql;
    }


    //如果是使用第一种方式注册的 这里提供一个接口获取本类中tables列表中的某个表，传入所需要获取的表名即可（不是重点）
    public Table getTableByName(String tableName)
    {
        int size = tables.size();
        if(size == 0)
        {
            return null;
        }else
        {
            for(int i = 0;i<size;i++)
            {
                if(tableName.equalsIgnoreCase(tables.get(i).table_name))
                {
                    return tables.get(i);
                }
            }
        }
        return null;
    }

}
