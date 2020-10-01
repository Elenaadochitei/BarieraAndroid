package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydb";
    private static final String TABLE_NUME = "PlateRegisterDetails";
    private static final String KEY_ID = "id";
    private static final String KEY_NUME = "name";
    private static final String KEY_NR_MASINA = "plateRegister";

    SQLiteDatabase database;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NUME + " ( " + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NUME + " TEXT, " + KEY_NR_MASINA + "TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NUME);
        onCreate(db);
    }
}
