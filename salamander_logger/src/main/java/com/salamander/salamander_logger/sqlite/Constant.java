package com.salamander.salamander_logger.sqlite;

import android.database.Cursor;

public class Constant {

    public static final int DATABASE_VERSION = 1;

    public static final String FORMAT_DB = "yyyy-MM-dd";
    public static final String FORMAT_DB_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DB_FULL_NO_SEC = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_UI = "EEEE, dd MMMM yyyy";
    public static final String FORMAT_UI_NO_DAY = "dd MMMM yyyy";

    public static final String TABLE_APP_LOG = "AppLog";
    public static final String LOG_ID = "ID";
    public static final String LOG_USER_ID = "UserID";
    public static final String LOG_WAKTU = "Waktu";
    public static final String LOG_HANDPHONE = "HPModel";
    public static final String LOG_OS = "OS";
    public static final String LOG_CLASS_NAME = "ClassName";
    public static final String LOG_LINE_NUMBER = "LineNumber";
    public static final String LOG_METHOD_NAME = "MethodName";
    public static final String LOG_EXCEPTION = "Exception";

    public static final String CREATE_TABLE_APP_LOG = "CREATE TABLE " + TABLE_APP_LOG + "(" +
            LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            LOG_USER_ID + " INTEGER, " +
            LOG_WAKTU + " DATETIME, " +
            LOG_HANDPHONE + " TEXT," +
            LOG_OS + " INTEGER," +
            LOG_CLASS_NAME + " TEXT," +
            LOG_LINE_NUMBER + " INTEGER," +
            LOG_METHOD_NAME + " TEXT," +
            LOG_EXCEPTION + " TEXT )";

    public static int getInt(Cursor cursor, String key) {
        return cursor.getInt(cursor.getColumnIndex(key));
    }

    public static String getString(Cursor cursor, String key) {
        return cursor.getString(cursor.getColumnIndex(key));
    }

    public static double getDouble(Cursor cursor, String key) {
        return cursor.getDouble(cursor.getColumnIndex(key));
    }
}