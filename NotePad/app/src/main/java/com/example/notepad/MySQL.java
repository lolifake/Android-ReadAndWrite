package com.example.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQL extends SQLiteOpenHelper {
    public MySQL( Context context, String name, SQLiteDatabase.CursorFactory
                 factory, int version){
        super(context,name,factory,version);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table logins(id integer primary key autoincrement," +
                "useraccount text, userpwd text)";//建立logins表
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
