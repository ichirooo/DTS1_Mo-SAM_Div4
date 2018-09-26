package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, ConstSQLite.DATABASE_NAME, null, ConstSQLite.DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(ConstSQLite.CREATE_TABLE_HEADER);
        db.execSQL(ConstSQLite.CREATE_TABLE_LINE);
        db.execSQL(ConstSQLite.CREATE_TABLE_TBL_CUSTOMER);
        db.execSQL(ConstSQLite.CREATE_TABLE_KEGIATAN);
        db.execSQL(ConstSQLite.CREATE_TABLE_PHOTO);
        db.execSQL(ConstSQLite.CREATE_TABLE_HOLIDAY);
        db.execSQL(ConstSQLite.CREATE_TABLE_TERMS_OF_PAYMENT);
        db.execSQL(ConstSQLite.CREATE_TABLE_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}