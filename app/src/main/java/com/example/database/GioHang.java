package com.example.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GioHang extends SQLiteOpenHelper {
    public GioHang(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Cursor getData(String sql)
    {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql,null);
    }
    public void QueryData(String sql)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
