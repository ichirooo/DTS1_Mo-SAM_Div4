package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.salamander.mo_sam_div4_dts1.object.Feedback;
import com.salamander.mo_sam_div4_dts1.object.Log_Feed;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by benny_aziz on 03/05/2015.
 */
public class LogSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public LogSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public boolean Insert(Log_Feed log) {
        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEY_LOG_LINE_ID, log.getFeedback().getLineID());
            values.put(ConstSQLite.KEY_LOG_EMPLOYEE_NO, log.getFeedback().getUser().getEmpNo());
            values.put(ConstSQLite.KEY_LOG_NOTE, log.getFeedback().getNote());
            values.put(ConstSQLite.KEY_LOG_DATETIME, new Tanggal(new Date()).getTglString());

            db.insert(ConstSQLite.TABLE_LOG, null, values);
            db.close();

            return true;
        } catch (Exception e) {
            Log.i("insert_log", e.toString());
            return false;
        }
    }

    public boolean Insert(ArrayList<Log_Feed> logs) {
        for (int i = 0; i < logs.size(); i++) {
            if (!Insert(logs.get(i)))
                return false;
        }
        return true;
    }

    public void Delete(int line_id) {
        try {
            db = dbHelper.getWritableDatabase();

            String query = " DELETE FROM " + ConstSQLite.TABLE_LOG +
                    " WHERE " + ConstSQLite.KEY_LOG_LINE_ID + " = " + String.valueOf(line_id);
            db.execSQL(query);
        } catch (Exception e) {
            e.toString();
        }
        db.close();
    }

    public ArrayList<Log_Feed> get(int lineID) {
        db = dbHelper.getReadableDatabase();
        ArrayList<Log_Feed> logs = new ArrayList<Log_Feed>();
        try {
            String query = " SELECT * FROM " + ConstSQLite.TABLE_LOG +
                    " WHERE " + ConstSQLite.KEY_LOG_LINE_ID + " = " + String.valueOf(lineID) +
                    " ORDER BY " + ConstSQLite.KEY_LOG_DATETIME + " DESC";
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    Feedback feedback = new Feedback();
                    Log_Feed log = new Log_Feed();

                    log.setID(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LOG_ID)));
                    log.setLogDate(new Tanggal(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, c.getString(c.getColumnIndex(ConstSQLite.KEY_LOG_DATETIME)))));

                    User user = new User();
                    user.setEmpNo(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LOG_EMPLOYEE_NO)));

                    feedback.setUser(user);
                    feedback.setLineID(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LOG_LINE_ID)));
                    feedback.getUser().setEmpNo(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LOG_EMPLOYEE_NO)));
                    feedback.setNote(c.getString(c.getColumnIndex(ConstSQLite.KEY_LOG_NOTE)));

                    log.setFeedback(feedback);
                    logs.add(log);
                } while (c.moveToNext());
            }
            db.close();
            c.close();
        } catch (Exception e) {
            db.close();
            logs = null;
        }
        return logs;
    }
}
