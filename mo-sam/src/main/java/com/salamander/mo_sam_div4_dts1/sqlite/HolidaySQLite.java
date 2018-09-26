package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salamander.mo_sam_div4_dts1.object.Holiday;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;

import java.util.ArrayList;
import java.util.Date;

public class HolidaySQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public HolidaySQLite(Context context) {
        dbHelper = new DatabaseHelper(context);
        this.context = context;
    }

    public void clear(int tahun) {
        db = dbHelper.getReadableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_HOLIDAY +
                " WHERE CAST(strftime('%Y', " + ConstSQLite.HOLIDAY_TANGGAL + ") AS INTEGER) = " + String.valueOf(tahun);
        db.execSQL(query);
        db.close();
    }

    private boolean Insert(Holiday holiday) {
        try {
            db = dbHelper.getWritableDatabase();
            long result = db.insertOrThrow(ConstSQLite.TABLE_HOLIDAY, null, holidayToContentValues(holiday));
            db.close();

            return true;
        } catch (Exception e) {
            //FileUtil.writeExceptionLog(context, HolidaySQLite.class.getSimpleName() + " => insert => ", e);
            return false;
        }
    }

    public boolean Post(Holiday holiday) {
        return Insert(holiday);
    }

    public Holiday get(Date tanggal) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT *  FROM " + ConstSQLite.TABLE_HOLIDAY + " WHERE " +
                ConstSQLite.HOLIDAY_TANGGAL + " = '" + DateUtils.dateToString(Tanggal.FORMAT_DATE, tanggal) + "'";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            return cursorToHoliday(c);
        }
        db.close();
        c.close();
        return null;
    }

    public ArrayList<Holiday> getMonthYear(int month, int tahun) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT *  FROM " + ConstSQLite.TABLE_HOLIDAY + " WHERE " +
                " CAST(strftime('%Y', " + ConstSQLite.HOLIDAY_TANGGAL + ") AS INTEGER) = " + String.valueOf(tahun) +
                " AND CAST(strftime('%m', " + ConstSQLite.HOLIDAY_TANGGAL + ") AS INTEGER) = " + String.valueOf(month) +
                " ORDER BY " + ConstSQLite.HOLIDAY_TANGGAL;

        ArrayList<Holiday> list_holiday = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list_holiday.add(cursorToHoliday(c));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_holiday;
    }

    public ArrayList<Holiday> getTahun(int tahun) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT *  FROM " + ConstSQLite.TABLE_HOLIDAY + " WHERE " +
                " CAST(strftime('%Y', " + ConstSQLite.HOLIDAY_TANGGAL + ") AS INTEGER) = " + String.valueOf(tahun) +
                " ORDER BY " + ConstSQLite.HOLIDAY_TANGGAL;

        ArrayList<Holiday> list_holiday = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list_holiday.add(cursorToHoliday(c));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_holiday;
    }

    private Holiday cursorToHoliday(Cursor cursor) {
        Holiday holiday = new Holiday();
        try {
            holiday.setTanggal(DateUtils.stringToDate(Tanggal.FORMAT_DATE, ConstSQLite.getString(cursor, ConstSQLite.HOLIDAY_TANGGAL)));
            holiday.setDescription(ConstSQLite.getString(cursor, ConstSQLite.HOLIDAY_DESCRIPTION));
        } catch (Exception e) {
            //FileUtil.writeExceptionLog(context, HolidaySQLite.class.getSimpleName() + " => cursorToHoliday => ", e);
            //Log.d(App.TAG, HolidaySQLite.class.getSimpleName() + " => cursorToHoliday  => " + e.toString());
        }
        return holiday;
    }

    private ContentValues holidayToContentValues(Holiday holiday) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstSQLite.HOLIDAY_TANGGAL, DateUtils.dateToString(Tanggal.FORMAT_DATE, holiday.getTanggal()));
        contentValues.put(ConstSQLite.HOLIDAY_DESCRIPTION, holiday.getDescription());
        return contentValues;
    }
}
