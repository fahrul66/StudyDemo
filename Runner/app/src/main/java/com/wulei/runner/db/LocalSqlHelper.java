package com.wulei.runner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wulei.runner.model.LocalSqlModel;
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
    public List<LocalSqlModel> query() {
        //集合
        List<LocalSqlModel> list = new ArrayList<>();
        //获取数据
        Cursor c = sqlRead.query(ConstantFactory.SQL_TABLE, null, null, null, null, null, "id desc");
        //指针调至第一个
        if (c.moveToFirst()) {
            do {
                /*
                 * 数据解析到读取
                 */
                int steps = c.getInt(c.getColumnIndex("steps"));
                String time = c.getString(c.getColumnIndex("time"));
                String km = c.getString(c.getColumnIndex("km"));
                String goals = c.getString(c.getColumnIndex("goals"));
                String picUrl = c.getString(c.getColumnIndex("picUrl"));
                //数据添加
                list.add(new LocalSqlModel(steps, time, km, goals, picUrl));
            } while (c.moveToNext());
        }
        return list;
    }

    /**
     * 插入数据
     */
    public void insert(LocalSqlModel data) {
        sqlWrite.beginTransaction();

        ContentValues c = new ContentValues();
        c.put("steps", data.getSteps());
        c.put("time", data.getTime());
        c.put("km", data.getKm());
        c.put("goals", data.getGoals());
        c.put("picUrl", data.getPicUrl());
        //写入数据
        sqlWrite.insert(ConstantFactory.SQL_TABLE, null, c);

        sqlWrite.setTransactionSuccessful();
        sqlWrite.endTransaction();
    }

    /**
     * 更改数据
     */
    public void update() {

    }

    /**
     * 删除数据
     */
    public void delete(LocalSqlModel data) {
        sqlWrite.delete(ConstantFactory.SQL_TABLE, "", null);
    }

    /**
     * 释放资源
     */
    public void close() {
        sqlWrite.close();
        sqlRead.close();
    }
}
