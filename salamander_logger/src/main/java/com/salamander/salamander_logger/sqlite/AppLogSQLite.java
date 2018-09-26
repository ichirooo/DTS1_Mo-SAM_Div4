package com.salamander.salamander_logger.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_logger.object.AppLog;

import java.util.Date;

public class AppLogSQLite {

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public AppLogSQLite(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public boolean Insert(AppLog appLog) {
        try {
            db = dbHelper.getWritableDatabase();
            long result = db.insertOrThrow(Constant.TABLE_APP_LOG, null, appLogToContentValues(appLog));
            db.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean Post(AppLog appLog) {
        return Insert(appLog);
    }

    public void clear() {
        db = dbHelper.getReadableDatabase();
        String query = "DELETE FROM " + Constant.TABLE_APP_LOG;
        db.execSQL(query);
        db.close();
    }

    private ContentValues appLogToContentValues(AppLog appLog) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.LOG_USER_ID, appLog.getUserID());
        contentValues.put(Constant.LOG_WAKTU, DateUtils.dateToString(Tanggal.FORMAT_DATETIME_FULL, new Date()));
        contentValues.put(Constant.LOG_CLASS_NAME, appLog.getClassName());
        contentValues.put(Constant.LOG_LINE_NUMBER, appLog.getLineNumber());
        contentValues.put(Constant.LOG_METHOD_NAME, appLog.getMethodName());
        contentValues.put(Constant.LOG_HANDPHONE, Build.MODEL);
        contentValues.put(Constant.LOG_OS, Build.VERSION.SDK_INT);
        contentValues.put(Constant.LOG_EXCEPTION, appLog.getException());
        return contentValues;
    }
}