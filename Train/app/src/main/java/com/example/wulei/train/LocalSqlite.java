package com.example.wulei.train;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wulei on 2016/11/7.
 */

public class LocalSqlite extends SQLiteOpenHelper {
    private Context context;//上下文
    //创建表
    private static final String CREATE_TABLE = "create table userTable (user text not null," +
            "password text not null," +
            "name text," +
            "sex text," +
            "college text," +
            "profession text," +
            "date date)";

    public LocalSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 判断登陆是否正确
     */
    public boolean login(String user, String password) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();//打开数据库
        Cursor cursor = sqLiteDatabase.query("userTable", null, null, null, null, null, null);//查询表
        while (cursor.moveToNext()) {
            String user1 = cursor.getString(cursor.getColumnIndex("user"));
            String password1 = cursor.getString(cursor.getColumnIndex("password"));
            if (user.equals(user1) && password.equals(password1)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 注册账号
     */
    public Boolean register(String user, String password, String name,
                            String sex, String college, String profession, String date) {
        //获得读权限
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //遍历查看用户名，姓名是否存在
        Cursor cursor = sqLiteDatabase.query("userTable", null, null, null, null, null, null);//查询表
        while (cursor.moveToNext()) {
            String user1 = cursor.getString(cursor.getColumnIndex("user"));
            String name1 = cursor.getString(cursor.getColumnIndex("name"));
            //用户名和姓名一样的话，结束
            if (user.equals(user1) || password.equals(name1)) {
                return false;
            }
        }
        //插入数据库
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("password", password);
        contentValues.put("name", name);
        contentValues.put("sex", sex);
        contentValues.put("college", college);
        contentValues.put("profession", profession);
        contentValues.put("date", date);
        sqLiteDatabase.insert("userTable", null, contentValues);
        return true;
    }
}
