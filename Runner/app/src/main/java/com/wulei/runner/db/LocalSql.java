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
     * 创建表
     */
    private static final String TABLE_NO_USER = "create Table if not exists " + ConstantFactory.SQL_TABLE + " (id integer PRIMARY KEY AUTOINCREMENT," +
            "steps integer ,time text,km text,goals text,picUrl text)";

    public LocalSql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_NO_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(TABLE_NO_USER);
            case 2:
            case 3:
        }
    }
}
