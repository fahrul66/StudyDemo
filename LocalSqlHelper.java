package com.wulei.runner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wulei.runner.model.LocalSqlPedometer;
import com.wulei.runner.model.LocalSqlRun;
import com.wulei.runner.utils.ConstantFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wule on 2017/04/07.
 */

public class LocalSqlHelper {
    //dataBase
    private final SQLiteDatabase sqlRead;
    private final SQLiteDatabase sqlWrite;

    public LocalSqlHelper(Context context) {

        LocalSql localSql = new LocalSql(context, ConstantFactory.SQL_NAME, null, ConstantFactory.SQL_VERSION);
        sqlRead = localSql.getReadableDatabase();
        sqlWrite = localSql.getWritableDatabase();
    }

    /**
     * 查询
     *
     * @return
     */
    public List<LocalSqlPedometer> queryJB(String selection, String selectionArgs, String order) {
        //集合
        List<LocalSqlPedometer> list = new ArrayList<>();
        //获取数据
        if (order == null) {

            order = "id desc";
        }
        //查询
        Cursor c = sqlRead.query(ConstantFactory.SQL_TABLE_JB, null, selection + "=?", new String[]{selectionArgs}, null, null, order);
        //指针调至第一个
        if (c.moveToFirst()) {
            do {
                /*
                 * 数据解析到读取
                 * steps integer,km integer,calorie integer,goals Integer,completed integer,date text
                 */
                int steps = c.getInt(c.getColumnIndex("steps"));
                double km = c.getDouble(c.getColumnIndex("km"));
                double calorie = c.getDouble(c.getColumnIndex("calorie"));
                int goals = c.getInt(c.getColumnIndex("goals"));
                String date = c.getString(c.getColumnIndex("date"));
                //数据添加
                list.add(new LocalSqlPedometer(steps, km, calorie, goals, date));
            } while (c.moveToNext());
        }
        return list;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<LocalSqlPedometer> queryJB(String order) {
        //集合
        List<LocalSqlPedometer> list = new ArrayList<>();
        //默认按照id 时间 逆序排序
        if (order == null) {

            order = "id desc";
        }
        //查询
        Cursor c = sqlRead.query(ConstantFactory.SQL_TABLE_JB, null, null, null, null, null, order);
        //指针调至第一个
        if (c.moveToFirst()) {
            do {
                /*
                 * 数据解析到读取
                 * steps integer,km integer,calorie integer,goals Integer,completed integer,date text
                 */
                int steps = c.getInt(c.getColumnIndex("steps"));
                double km = c.getDouble(c.getColumnIndex("km"));
                double calorie = c.getDouble(c.getColumnIndex("calorie"));
                int goals = c.getInt(c.getColumnIndex("goals"));
                String date = c.getString(c.getColumnIndex("date"));
                //数据添加
                list.add(new LocalSqlPedometer(steps, km, calorie, goals, date));
            } while (c.moveToNext());
        }
        return list;
    }

    /**
     * 查询跑步
     *
     * @param selection
     * @param selectionArgs
     * @param order
     * @return
     */
    public List<LocalSqlRun> queryPB(String selection, String selectionArgs, String order) {
        //集合
        List<LocalSqlRun> list = new ArrayList<>();
        //获取数据
        if (order == null) {

            order = "id desc";
        }
        //查询
        Cursor c = sqlRead.query(ConstantFactory.SQL_TABLE_PB, null, selection + "=?", new String[]{selectionArgs}, null, null, order);
        //指针调至第一个
        if (c.moveToFirst()) {
            do {
                /*
                 * 数据解析到读取
                 * time text,km integer,speed integer,picUrl text,date text,address text
                 */
                String time = c.getString(c.getColumnIndex("time"));
                double km = c.getDouble(c.getColumnIndex("km"));
                double speed = c.getDouble(c.getColumnIndex("speed"));
                String picUrl = c.getString(c.getColumnIndex("picUrl"));
                String address = c.getString(c.getColumnIndex("address"));
                String date = c.getString(c.getColumnIndex("date"));
                //数据添加
                list.add(new LocalSqlRun(time, km, speed, picUrl, date, address));
            } while (c.moveToNext());
        }
        return list;
    }

    public List<LocalSqlRun> queryPB(String order) {
        //集合
        List<LocalSqlRun> list = new ArrayList<>();
        //获取数据
        if (order == null) {

            order = "id desc";
        }
        //查询
        Cursor c = sqlRead.query(ConstantFactory.SQL_TABLE_PB, null, null, null, null, null, order);
        //指针调至第一个
        if (c.moveToFirst()) {
            do {
                /*
                 * 数据解析到读取
                 * time text,km integer,speed integer,picUrl text,date text,address text
                 */
                String time = c.getString(c.getColumnIndex("time"));
                double km = c.getDouble(c.getColumnIndex("km"));
                double speed = c.getDouble(c.getColumnIndex("speed"));
                String picUrl = c.getString(c.getColumnIndex("picUrl"));
                String address = c.getString(c.getColumnIndex("address"));
                String date = c.getString(c.getColumnIndex("date"));
                //数据添加
                list.add(new LocalSqlRun(time, km, speed, picUrl, date, address));
            } while (c.moveToNext());
        }
        return list;
    }

    /**
     * 插入数据
     */
    public void insert(LocalSqlPedometer data) {
        sqlWrite.beginTransaction();

        ContentValues c = new ContentValues();
        c.put("steps", data.getSteps());
        c.put("km", data.getKm());
        c.put("calorie", data.getCalorie());
        c.put("goals", data.getGoals());
        c.put("date", data.getDate());
        //写入数据
        sqlWrite.insert(ConstantFactory.SQL_TABLE_JB, null, c);

        sqlWrite.setTransactionSuccessful();
        sqlWrite.endTransaction();
    }

    /**
     * 插入数据,time text,km integer,speed integer,picUrl text,date text,address text
     */
    public void insert(LocalSqlRun data) {
        sqlWrite.beginTransaction();

        ContentValues c = new ContentValues();
        c.put("time", data.getTime());
        c.put("km", data.getKm());
        c.put("speed", data.getSpeed());
        c.put("picUrl", data.getPicUrl());
        c.put("address", data.getAddress());
        c.put("date", data.getDate());
        //写入数据
        sqlWrite.insert(ConstantFactory.SQL_TABLE_PB, null, c);

        sqlWrite.setTransactionSuccessful();
        sqlWrite.endTransaction();
    }

    /**
     * 更改数据,更新计步数据
     */
    public void update(String table, String key, String value, String where, String whereArgs) {
        /*
         * 事务
         */
        sqlWrite.beginTransaction();
        //数据更新
        ContentValues c = new ContentValues();
        c.put(key, value);
        sqlWrite.update(table, c, where + "=?", new String[]{whereArgs});
        //事务结束
        sqlWrite.setTransactionSuccessful();
        sqlWrite.endTransaction();
    }

    public void update(String table, String key, int value, String where, String whereArgs) {
        /*
         * 事务
         */
        sqlWrite.beginTransaction();
        //数据更新
        ContentValues c = new ContentValues();
        c.put(key, value);
        sqlWrite.update(table, c, where + "=?", new String[]{whereArgs});
        //事务结束
        sqlWrite.setTransactionSuccessful();
        sqlWrite.endTransaction();
    }

    public void update(String table, String key, double value, String where, String whereArgs) {
        /*
         * 事务
         */
        sqlWrite.beginTransaction();
        //数据更新
        ContentValues c = new ContentValues();
        c.put(key, value);
        sqlWrite.update(table, c, where + "=?", new String[]{whereArgs});
        //事务结束
        sqlWrite.setTransactionSuccessful();
        sqlWrite.endTransaction();
    }

    /**
     * 删除数据
     */
    public void delete(String table, String where, String whereArgs) {
        sqlWrite.delete(table, where + "=?", new String[]{whereArgs});
    }

    /**
     * 释放资源
     */
    public void close() {
        sqlWrite.close();
        sqlRead.close();
    }
}
