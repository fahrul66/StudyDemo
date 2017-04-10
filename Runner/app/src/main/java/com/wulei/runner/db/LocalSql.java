package com.wulei.runner.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wulei.runner.utils.ConstantFactory;

/**
 * Created by wule on 2017/04/07.
 */

public class LocalSql extends SQLiteOpenHelper {
    /*
     * 创建表计步，跑步表
     */
    private static final String SQL_JB = "create Table " + ConstantFactory.SQL_TABLE_JB + " (id integer PRIMARY KEY AUTOINCREMENT," +
            "steps integer,km REAL,calorie REAL,goals Integer,date text)";

    private static final String SQL_PB = "create Table " + ConstantFactory.SQL_TABLE_PB + "(id integer PRIMARY KEY AUTOINCREMENT," +
            "time text,km REAL,speed REAL,picUrl text,date text,address text)";

    public LocalSql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_JB);
        db.execSQL(SQL_PB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(SQL_JB);
                db.execSQL(SQL_PB);
            case 2:
            case 3:
        }
    }
}
