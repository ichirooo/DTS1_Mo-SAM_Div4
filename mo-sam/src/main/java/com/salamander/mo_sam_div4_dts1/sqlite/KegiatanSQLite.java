package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Const;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.SalesHeader;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class KegiatanSQLite {

    public static final int TEMP_ID_START = 1000000;

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public KegiatanSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public Kegiatan insert(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            db.insert(ConstSQLite.TABLE_KEGIATAN, null, kegiatanToContentValues(kegiatan));
            db.close();

            return get(kegiatan.getIDServer());
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return kegiatan;
        }
    }

    public Kegiatan update(Kegiatan kegiatan) {
        try {
            Kegiatan prevKegiatan = get(kegiatan.getIDServer());
            db = dbHelper.getWritableDatabase();
            db.update(ConstSQLite.TABLE_KEGIATAN, kegiatanToContentValues(kegiatan), Kegiatan.KEGIATAN_ID_SERVER + " = ?",
                    new String[]{String.valueOf(kegiatan.getIDServer())});
            db.close();

            if (kegiatan.isCheckedIn())
                App.getSession(context).setCheckedInKegiatan(kegiatan.getIDServer());
            else if (prevKegiatan.isCheckedIn())
                App.getSession(context).setCheckedInKegiatan(0);

            return get(kegiatan.getIDServer());
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return kegiatan;
        }
    }

    public ArrayList<Kegiatan> Read(int when) {

        db = dbHelper.getReadableDatabase();
        ArrayList<Kegiatan> listkegiatan = new ArrayList<>();
        String query = "";

        switch (when) {
            case Const.KEGIATAN_TODAY:
                query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                        User.BEX_EMP_NO + " = '" + App.getUser(context).getEmpNo() + "' AND " +
                        User.BEX_INITIAL + " = '" + App.getUser(context).getInitial() + "' AND " +
                        " DATE(" + Kegiatan.KEGIATAN_TGL_MULAI + ") = DATE('now', 'localtime')" +
                        " ORDER BY " + Kegiatan.KEGIATAN_CANCELED + " ASC," + Kegiatan.KEGIATAN_TGL_MULAI + " DESC";
                break;
            case Const.KEGIATAN_THIS_MONTH:
                query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                        User.BEX_EMP_NO + " = '" + App.getUser(context).getEmpNo() + "' AND " +
                        User.BEX_INITIAL + " = '" + App.getUser(context).getInitial() + "' AND " +
                        " DATE(" + Kegiatan.KEGIATAN_TGL_MULAI + ") BETWEEN " +
                        "(SELECT DATE('now', 'localtime', 'start of month')) AND " +
                        "(SELECT DATE('now', 'localtime', 'start of month','+1 month','-1 day')) " +
                        " ORDER BY " + Kegiatan.KEGIATAN_CANCELED + " ASC," + Kegiatan.KEGIATAN_TGL_MULAI + " DESC";
                break;
        }

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                try {
                    listkegiatan.add(cursorToKegiatan(c));
                } catch (Exception e) {
                    // TODO: handle exception
                    query = e.toString();
                }
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return listkegiatan;
    }

    public ArrayList<Kegiatan> Read(Calendar calendar, int range) {

        db = dbHelper.getReadableDatabase();
        ArrayList<Kegiatan> listkegiatan = new ArrayList<>();
        String query = null;
        switch (range) {
            case Const.KEGIATAN_BY_DATE:
                query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                        User.BEX_EMP_NO + " = '" + App.getUser(context).getEmpNo() + "' AND " +
                        User.BEX_INITIAL + " = '" + App.getUser(context).getInitial() + "' AND " +
                        " CAST(STRFTIME('%d', " + Kegiatan.KEGIATAN_TGL_MULAI + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + " AND " +
                        " CAST(STRFTIME('%m', " + Kegiatan.KEGIATAN_TGL_MULAI + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.MONTH) + 1) + " AND " +
                        " CAST(STRFTIME('%Y', " + Kegiatan.KEGIATAN_TGL_MULAI + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.YEAR)) +
                        " ORDER BY " + Kegiatan.KEGIATAN_CANCELED + " ASC," + Kegiatan.KEGIATAN_TGL_MULAI + " DESC";
                break;
            case Const.KEGIATAN_BY_MONTH:
                query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                        User.BEX_EMP_NO + " = '" + String.valueOf(App.getUser(context).getEmpNo()) + "' AND " +
                        User.BEX_INITIAL + " = '" + App.getUser(context).getInitial() + "' AND " +
                        " CAST(STRFTIME('%m', " + Kegiatan.KEGIATAN_TGL_MULAI + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.MONTH) + 1) + " AND " +
                        " CAST(STRFTIME('%Y', " + Kegiatan.KEGIATAN_TGL_MULAI + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.YEAR)) +
                        " ORDER BY " + Kegiatan.KEGIATAN_CANCELED + " ASC," + Kegiatan.KEGIATAN_TGL_MULAI + " DESC";
                break;
        }

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                try {
                    listkegiatan.add(cursorToKegiatan(c));
                } catch (Exception e) {
                    // TODO: handle exception
                    query = e.toString();
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return listkegiatan;
    }

    public ArrayList<Kegiatan> getByDate(Date date) {
        db = dbHelper.getReadableDatabase();
        String tanggal = DateUtils.dateToString(Tanggal.FORMAT_DATE, date);
        ArrayList<Kegiatan> listkegiatan = new ArrayList<>();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN +
                " WHERE " + User.BEX_EMP_NO + " = '" + String.valueOf(App.getUser(context).getEmpNo()) + "'" +
                " AND " + Kegiatan.KEGIATAN_ID_SERVER + " < " + String.valueOf(TEMP_ID_START) +
                " AND DATE(" + Kegiatan.KEGIATAN_TGL_MULAI + ") = DATE('" + tanggal + "')" +
                " ORDER BY " + Kegiatan.KEGIATAN_CANCELED + " ASC," +
                Kegiatan.KEGIATAN_TGL_MULAI + " DESC," +
                Kegiatan.KEGIATAN_ID_SERVER + " DESC";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                listkegiatan.add(cursorToKegiatan(c));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return listkegiatan;
    }

    public boolean Cancel(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Kegiatan.KEGIATAN_CANCELED, kegiatan.getCancel());
            values.put(Kegiatan.KEGIATAN_CANCEL_REASON, kegiatan.getCancelReason());

            db.update(ConstSQLite.TABLE_KEGIATAN, values, Kegiatan.KEGIATAN_ID_SERVER + " = ?",
                    new String[]{String.valueOf(kegiatan.getIDServer())});
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return false;
        }
    }

    public boolean Keterangan(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Kegiatan.KEGIATAN_KETERANGAN, kegiatan.getKeterangan());

            db.update(ConstSQLite.TABLE_KEGIATAN, values, Kegiatan.KEGIATAN_ID_SERVER + " = ?",
                    new String[]{String.valueOf(kegiatan.getIDServer())});
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return false;
        }
    }

    public boolean Result(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Kegiatan.KEGIATAN_RESULT, kegiatan.getResult());

            db.update(ConstSQLite.TABLE_KEGIATAN, values, Kegiatan.KEGIATAN_ID_SERVER + " = ?",
                    new String[]{String.valueOf(kegiatan.getIDServer())});
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return false;
        }
    }

    public void delete(int kegId) {
        db = dbHelper.getWritableDatabase();
        new CustomerSQLite(context).deleteByKegiatan(kegId);
        new SalesHeaderSQLite(context).deleteFromKegiatan(kegId);
        String query = "DELETE FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                Kegiatan.KEGIATAN_ID_SERVER + " = " + String.valueOf(kegId);
        db.execSQL(query);
        db.close();
    }

    public Kegiatan Post(Kegiatan kegiatan) {
        new SalesHeaderSQLite(context).post(kegiatan.getSalesHeader());
        if (isExist(kegiatan))
            return update(kegiatan);
        else
            return insert(kegiatan);
    }

    public boolean isExist(Kegiatan kegiatan) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                Kegiatan.KEGIATAN_ID_SERVER + " = " + String.valueOf(kegiatan.getIDServer());
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        db.close();
        c.close();
        return exist;
    }

    public Kegiatan get(int kid) {
        db = dbHelper.getReadableDatabase();
        Kegiatan kegiatan = new Kegiatan();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                Kegiatan.KEGIATAN_ID_SERVER + " = " + String.valueOf(kid);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            try {
                kegiatan = cursorToKegiatan(cursor);
            } catch (Exception e) {
                // TODO: handle exception
                query = e.toString();
            }
        } else kegiatan = null;
        cursor.close();
        db.close();
        return kegiatan;
    }

    public ArrayList<Integer> getOpen(String bex_no) {
        db = dbHelper.getReadableDatabase();
        ArrayList<Integer> list = new ArrayList<Integer>();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " K LEFT JOIN " +
                ConstSQLite.TABLE_HEADER + " H ON K." + Kegiatan.KEGIATAN_ID_SERVER + "=H." + SalesHeader.SALES_HEADER_ID_KEGIATAN +
                " WHERE K." + User.BEX_EMP_NO + "='" + bex_no + "'" +
                " AND " + SalesHeader.SALES_HEADER_STATUS + "<> 1";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                try {
                    int id;
                    id = c.getInt(c.getColumnIndex(SalesHeader.SALES_HEADER_ID_KEGIATAN));
                    list.add(id);
                } catch (Exception e) {
                    // TODO: handle exception
                    query = e.toString();
                }

            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list;
    }

    public boolean isClosed(int id) {
        db = dbHelper.getReadableDatabase();
        boolean result = false;
        String query = "SELECT K." + Kegiatan.KEGIATAN_CANCELED + " AS Cancel, H." + SalesHeader.SALES_HEADER_STATUS + " AS Status" +
                " FROM " + ConstSQLite.TABLE_KEGIATAN + " K LEFT JOIN " +
                ConstSQLite.TABLE_HEADER + " H ON K." + Kegiatan.KEGIATAN_ID_SERVER + "=H." + SalesHeader.SALES_HEADER_ID_KEGIATAN +
                " WHERE K." + Kegiatan.KEGIATAN_ID_SERVER + "='" + Integer.toString(id) + "'";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            try {
                if (c.getInt(c.getColumnIndex("Cancel")) != 0 || c.getInt(c.getColumnIndex("Status")) == 1) {
                    result = true;
                }
            } catch (Exception e) {
                // TODO: handle exception
                query = e.toString();
            }
        }
        db.close();
        c.close();
        return result;
    }

    public boolean isCOS(int idKegiatan) {
        db = dbHelper.getReadableDatabase();
        boolean result = false;
        String query = "SELECT " + SalesHeader.SALES_HEADER_STATUS +
                " FROM " + ConstSQLite.TABLE_HEADER +
                " WHERE " + SalesHeader.SALES_HEADER_ID_KEGIATAN + "=" + Integer.toString(idKegiatan);

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            try {
                if (c.getInt(c.getColumnIndex(SalesHeader.SALES_HEADER_STATUS)) == 1) {
                    result = true;
                }
            } catch (Exception e) {
                // TODO: handle exception
                query = e.toString();
            }
        } else result = false;
        db.close();
        c.close();
        return result;
    }

    public void checkIn(int id_kegiatan) {
        db = dbHelper.getWritableDatabase();
        String query = "UPDATE " + ConstSQLite.TABLE_KEGIATAN +
                " SET " + Kegiatan.KEGIATAN_CHECKED_IN + " = 0" +
                " WHERE " + User.BEX_EMP_NO + " = " + String.valueOf(App.getUser(context).getEmpNo());
        db.execSQL(query);
        query = "UPDATE " + ConstSQLite.TABLE_KEGIATAN +
                " SET " + Kegiatan.KEGIATAN_CHECKED_IN + " = 1" +
                " WHERE " + Kegiatan.KEGIATAN_ID_SERVER + " = " + String.valueOf(id_kegiatan);
        db.execSQL(query);
        App.getSession(context).setCheckedInKegiatan(id_kegiatan);
    }

    public void checkOut(int id_kegiatan) {
        db = dbHelper.getWritableDatabase();
        String query = "UPDATE " + ConstSQLite.TABLE_KEGIATAN +
                " SET " + Kegiatan.KEGIATAN_CHECKED_IN + " = 0" +
                " WHERE " + Kegiatan.KEGIATAN_ID_SERVER + " = " + String.valueOf(id_kegiatan);
        db.execSQL(query);
        App.getSession(context).setCheckedInKegiatan(0);
    }

    public ArrayList<Integer> getAllIDAsArray(String startDate, String endDate) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT  * " +
                " FROM    " + ConstSQLite.TABLE_KEGIATAN +
                " WHERE   " + User.BEX_EMP_NO + " = " + String.valueOf(App.getUser(context).getEmpNo()) +
                " AND DATE(" + Kegiatan.KEGIATAN_TGL_MULAI + ") BETWEEN DATE('" + startDate + "') AND DATE('" + endDate + "') " +
                " ORDER BY " + Kegiatan.KEGIATAN_ID_SERVER + " DESC";

        ArrayList<Integer> list_id_kegiatan = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list_id_kegiatan.add(ConstSQLite.getInt(c, Kegiatan.KEGIATAN_ID_SERVER));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_id_kegiatan;
    }

    private ContentValues kegiatanToContentValues(Kegiatan kegiatan) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Kegiatan.KEGIATAN_ID_SERVER, kegiatan.getIDServer());
        contentValues.put(Kegiatan.KEGIATAN_TGL_MULAI, kegiatan.getStartDate().getTglString());
        contentValues.put(Kegiatan.KEGIATAN_TGL_SELESAI, kegiatan.getEndDate().getTglString());
        contentValues.put(User.BEX_EMP_NO, kegiatan.getSalesperson().getEmpNo());
        contentValues.put(User.BEX_INITIAL, kegiatan.getSalesperson().getInitial());
        contentValues.put(Kegiatan.KEGIATAN_TIPE, kegiatan.getTipe());
        contentValues.put(Kegiatan.KEGIATAN_JENIS, kegiatan.getJenis());
        contentValues.put(Kegiatan.KEGIATAN_KETERANGAN, kegiatan.getKeterangan());
        contentValues.put(Kegiatan.KEGIATAN_SUBJECT, kegiatan.getSubject());
        contentValues.put(Kegiatan.KEGIATAN_CANCELED, kegiatan.getCancel());
        contentValues.put(Kegiatan.KEGIATAN_CANCEL_REASON, kegiatan.getCancelReason());
        contentValues.put(Kegiatan.KEGIATAN_CANCEL_DATE, kegiatan.getCancelDate().getTglString());
        contentValues.put(Kegiatan.KEGIATAN_RESULT, kegiatan.getResult());
        contentValues.put(Kegiatan.KEGIATAN_RESULT_DATE, kegiatan.getResultDate().getTglString());
        contentValues.put(Kegiatan.KEGIATAN_CHECKED_IN, kegiatan.getCheckedIn());
        contentValues.put(Kegiatan.KEGIATAN_INPUT_DATE, kegiatan.getInputDate().getTglString());
        return contentValues;
    }

    private Kegiatan cursorToKegiatan(Cursor cursor) {
        Kegiatan kegiatan = new Kegiatan();
        kegiatan.setIDServer(ConstSQLite.getInt(cursor, Kegiatan.KEGIATAN_ID_SERVER));
        kegiatan.setIDLokal(ConstSQLite.getInt(cursor, Kegiatan.KEGIATAN_ID_LOKAL));
        kegiatan.setTipe(ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_TIPE));
        kegiatan.setJenis(ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_JENIS));
        kegiatan.setSubject(ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_SUBJECT));
        kegiatan.setKeterangan(ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_KETERANGAN));
        kegiatan.setStartDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_TGL_MULAI)));
        kegiatan.setEndDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_TGL_SELESAI)));
        kegiatan.setResult(ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_RESULT));
        kegiatan.setResultDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_RESULT_DATE)));
        kegiatan.setCancel(ConstSQLite.getInt(cursor, Kegiatan.KEGIATAN_CANCELED));
        kegiatan.setCancelReason(ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_CANCEL_REASON));
        kegiatan.setCancelDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, ConstSQLite.getString(cursor, Kegiatan.KEGIATAN_CANCEL_DATE)));
        kegiatan.setCheckedIn(ConstSQLite.getInt(cursor, Kegiatan.KEGIATAN_CHECKED_IN));
        //kegiatan.getSalesHeader().setCustomer(new CustomerSQLite(context).get(kegiatan.getIDServer()));
        kegiatan.setSalesHeader(new SalesHeaderSQLite(context).getFromKegiatan(kegiatan.getIDServer()));
        if (App.getUser(context).getEmpNo() == ConstSQLite.getInt(cursor, User.BEX_EMP_NO))
            kegiatan.setSalesperson(App.getUser(context));
        else
            kegiatan.setSalesperson(new User(ConstSQLite.getInt(cursor, User.BEX_EMP_NO), ConstSQLite.getString(cursor, User.BEX_INITIAL)));
        return kegiatan;
    }
}