package co.id.datascrip.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import co.id.datascrip.mo_sam_div4_dts1.Const;
import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.function.Waktu;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.object.SalesHeader;

public class KegiatanSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public KegiatanSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public boolean Insert(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEG_ID, kegiatan.getID());

            values.put(ConstSQLite.KEY_BEX_NO, kegiatan.getBEX().getEmpNo());
            values.put(ConstSQLite.KEY_BEX_INITIAL, kegiatan.getBEX().getInitial());

            values.put(ConstSQLite.KEG_TIPE, kegiatan.getTipe());
            values.put(ConstSQLite.KEG_JENIS, kegiatan.getJenis());
            values.put(ConstSQLite.KEG_SUBJECT, kegiatan.getSubject());
            values.put(ConstSQLite.KEG_KETERANGAN, kegiatan.getKeterangan());
            values.put(ConstSQLite.KEG_RESULT, kegiatan.getResult());
            values.put(ConstSQLite.KEG_CANCEL, kegiatan.getCancel());
            values.put(ConstSQLite.KEG_CANCEL_REASON, kegiatan.getReason());
            values.put(ConstSQLite.KEG_CHECK_IN, kegiatan.getCheckIn());
            values.put(ConstSQLite.KEG_START_DATE, kegiatan.getStartDate());
            values.put(ConstSQLite.KEG_END_DATE, kegiatan.getEndDate());
            values.put(ConstSQLite.INPUT_DATE, new Waktu().getCurrent());

            db.insert(ConstSQLite.TABLE_KEGIATAN, null, values);
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return false;
        }
    }

    public boolean Update(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(ConstSQLite.KEY_BEX_NO, kegiatan.getBEX().getEmpNo());
            values.put(ConstSQLite.KEY_BEX_INITIAL, kegiatan.getBEX().getInitial());

            values.put(ConstSQLite.KEG_TIPE, kegiatan.getTipe());
            values.put(ConstSQLite.KEG_JENIS, kegiatan.getJenis());
            values.put(ConstSQLite.KEG_SUBJECT, kegiatan.getSubject());
            values.put(ConstSQLite.KEG_KETERANGAN, kegiatan.getKeterangan());
            values.put(ConstSQLite.KEG_RESULT, kegiatan.getResult());
            values.put(ConstSQLite.KEG_CANCEL, kegiatan.getCancel());
            values.put(ConstSQLite.KEG_CANCEL_REASON, kegiatan.getReason());
            values.put(ConstSQLite.KEG_CHECK_IN, kegiatan.getCheckIn());
            values.put(ConstSQLite.KEG_START_DATE, kegiatan.getStartDate());
            values.put(ConstSQLite.KEG_END_DATE, kegiatan.getEndDate());
            values.put(ConstSQLite.INPUT_DATE, new Waktu().getCurrent());

            db.update(ConstSQLite.TABLE_KEGIATAN, values, ConstSQLite.KEG_ID + " = ?",
                    new String[]{String.valueOf(kegiatan.getID())});
            db.close();

            if (kegiatan.getCheckIn() == Const.CHECK_IN)
                Function.sendLocation(context, kegiatan.getID());
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return false;
        }
    }

    public ArrayList<Kegiatan> Read(int when) {

        db = dbHelper.getReadableDatabase();
        ArrayList<Kegiatan> listkegiatan = new ArrayList<Kegiatan>();
        String query = "";

        switch (when) {
            case Const.KEGIATAN_TODAY:
                query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                        ConstSQLite.KEY_BEX_NO + " = '" + Global.getBEX(context).getEmpNo() + "' AND " +
                        ConstSQLite.KEY_BEX_INITIAL + " = '" + Global.getBEX(context).getInitial() + "' AND " +
                        " DATE(" + ConstSQLite.KEG_START_DATE + ") = DATE('now', 'localtime')" +
                        " ORDER BY " + ConstSQLite.KEG_CANCEL + " ASC," + ConstSQLite.KEG_START_DATE + " DESC";
                break;
            case Const.KEGIATAN_THIS_MONTH:
                query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                        ConstSQLite.KEY_BEX_NO + " = '" + Global.getBEX(context).getEmpNo() + "' AND " +
                        ConstSQLite.KEY_BEX_INITIAL + " = '" + Global.getBEX(context).getInitial() + "' AND " +
                        " DATE(" + ConstSQLite.KEG_START_DATE + ") BETWEEN " +
                        "(SELECT DATE('now', 'localtime', 'start of month')) AND " +
                        "(SELECT DATE('now', 'localtime', 'start of month','+1 month','-1 day')) " +
                        " ORDER BY " + ConstSQLite.KEG_CANCEL + " ASC," + ConstSQLite.KEG_START_DATE + " DESC";
                break;
        }

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                try {
                    Kegiatan kegiatan = new Kegiatan();

                    SalesHeader sales_header = new SalesHeaderSQLite(context).get(c.getInt(c.getColumnIndex(ConstSQLite.KEG_ID)));

                    kegiatan.setID(c.getInt(c.getColumnIndex(ConstSQLite.KEG_ID)));
                    kegiatan.setTipe(c.getString(c.getColumnIndex(ConstSQLite.KEG_TIPE)));
                    kegiatan.setJenis(c.getString(c.getColumnIndex(ConstSQLite.KEG_JENIS)));
                    kegiatan.setSubject(c.getString(c.getColumnIndex(ConstSQLite.KEG_SUBJECT)));
                    kegiatan.setKeterangan(c.getString(c.getColumnIndex(ConstSQLite.KEG_KETERANGAN)));
                    kegiatan.setResult(c.getString(c.getColumnIndex(ConstSQLite.KEG_RESULT)));
                    kegiatan.setCancel(c.getInt(c.getColumnIndex(ConstSQLite.KEG_CANCEL)));
                    kegiatan.setReason(c.getString(c.getColumnIndex(ConstSQLite.KEG_CANCEL_REASON)));
                    kegiatan.setStartDate(c.getString(c.getColumnIndex(ConstSQLite.KEG_START_DATE)));
                    kegiatan.setEndDate(c.getString(c.getColumnIndex(ConstSQLite.KEG_END_DATE)));
                    kegiatan.setCheckIn(c.getInt(c.getColumnIndex(ConstSQLite.KEG_CHECK_IN)));

                    kegiatan.setSalesHeader(sales_header);
                    kegiatan.setBEX(Global.getBEX(context));

                    listkegiatan.add(kegiatan);

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
        ArrayList<Kegiatan> listkegiatan = new ArrayList<Kegiatan>();
        String query = null;
        switch (range) {
            case Const.KEGIATAN_BY_DATE:
                query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                        ConstSQLite.KEY_BEX_NO + " = '" + Global.getBEX(context).getEmpNo() + "' AND " +
                        ConstSQLite.KEY_BEX_INITIAL + " = '" + Global.getBEX(context).getInitial() + "' AND " +
                        " CAST(STRFTIME('%d', " + ConstSQLite.KEG_START_DATE + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + " AND " +
                        " CAST(STRFTIME('%m', " + ConstSQLite.KEG_START_DATE + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.MONTH) + 1) + " AND " +
                        " CAST(STRFTIME('%Y', " + ConstSQLite.KEG_START_DATE + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.YEAR)) +
                        " ORDER BY " + ConstSQLite.KEG_CANCEL + " ASC," + ConstSQLite.KEG_START_DATE + " DESC";
                break;
            case Const.KEGIATAN_BY_MONTH:
                query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                        ConstSQLite.KEY_BEX_NO + " = '" + Global.getBEX(context).getEmpNo() + "' AND " +
                        ConstSQLite.KEY_BEX_INITIAL + " = '" + Global.getBEX(context).getInitial() + "' AND " +
                        " CAST(STRFTIME('%m', " + ConstSQLite.KEG_START_DATE + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.MONTH) + 1) + " AND " +
                        " CAST(STRFTIME('%Y', " + ConstSQLite.KEG_START_DATE + ") AS INTEGER) = " +
                        Integer.toString(calendar.get(Calendar.YEAR)) +
                        " ORDER BY " + ConstSQLite.KEG_CANCEL + " ASC," + ConstSQLite.KEG_START_DATE + " DESC";
                break;
        }

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                try {
                    Kegiatan kegiatan = new Kegiatan();

                    SalesHeader sales_header = new SalesHeaderSQLite(context).get(c.getInt(c.getColumnIndex(ConstSQLite.KEG_ID)));

                    kegiatan.setID(c.getInt(c.getColumnIndex(ConstSQLite.KEG_ID)));
                    kegiatan.setTipe(c.getString(c.getColumnIndex(ConstSQLite.KEG_TIPE)));
                    kegiatan.setJenis(c.getString(c.getColumnIndex(ConstSQLite.KEG_JENIS)));
                    kegiatan.setSubject(c.getString(c.getColumnIndex(ConstSQLite.KEG_SUBJECT)));
                    kegiatan.setKeterangan(c.getString(c.getColumnIndex(ConstSQLite.KEG_KETERANGAN)));
                    kegiatan.setResult(c.getString(c.getColumnIndex(ConstSQLite.KEG_RESULT)));
                    kegiatan.setCancel(c.getInt(c.getColumnIndex(ConstSQLite.KEG_CANCEL)));
                    kegiatan.setReason(c.getString(c.getColumnIndex(ConstSQLite.KEG_CANCEL_REASON)));
                    kegiatan.setStartDate(c.getString(c.getColumnIndex(ConstSQLite.KEG_START_DATE)));
                    kegiatan.setEndDate(c.getString(c.getColumnIndex(ConstSQLite.KEG_END_DATE)));
                    kegiatan.setCheckIn(c.getInt(c.getColumnIndex(ConstSQLite.KEG_CHECK_IN)));

                    kegiatan.setSalesHeader(sales_header);
                    kegiatan.setBEX(Global.getBEX(context));

                    listkegiatan.add(kegiatan);

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

    public boolean Cancel(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEG_CANCEL, kegiatan.getCancel());
            values.put(ConstSQLite.KEG_CANCEL_REASON, kegiatan.getReason());

            db.update(ConstSQLite.TABLE_KEGIATAN, values, ConstSQLite.KEG_ID + " = ?",
                    new String[]{String.valueOf(kegiatan.getID())});
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
            values.put(ConstSQLite.KEG_KETERANGAN, kegiatan.getKeterangan());

            db.update(ConstSQLite.TABLE_KEGIATAN, values, ConstSQLite.KEG_ID + " = ?",
                    new String[]{String.valueOf(kegiatan.getID())});
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
            values.put(ConstSQLite.KEG_RESULT, kegiatan.getResult());

            db.update(ConstSQLite.TABLE_KEGIATAN, values, ConstSQLite.KEG_ID + " = ?",
                    new String[]{String.valueOf(kegiatan.getID())});
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return false;
        }
    }

    public void Delete(int kegId) {
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                ConstSQLite.KEG_ID + " = " + String.valueOf(kegId);
        db.execSQL(query);
        db.close();
    }

    public boolean Post(Kegiatan kegiatan) {
        new SalesHeaderSQLite(context).Post(kegiatan);
        if (isExist(kegiatan))
            return Update(kegiatan);
        else
            return Insert(kegiatan);
    }

    public boolean isExist(Kegiatan kegiatan) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                ConstSQLite.KEG_ID + " = " + String.valueOf(kegiatan.getID());
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        db.close();
        c.close();
        return exist;
    }

    public Kegiatan get(int kid) {
        db = dbHelper.getReadableDatabase();
        Kegiatan keg = new Kegiatan();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                ConstSQLite.KEG_ID + " = " + String.valueOf(kid);

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            try {

                SalesHeader sales_header = new SalesHeaderSQLite(context).get(kid);

                keg.setID(c.getInt(c.getColumnIndex(ConstSQLite.KEG_ID)));
                keg.setTipe(c.getString(c.getColumnIndex(ConstSQLite.KEG_TIPE)));
                keg.setJenis(c.getString(c.getColumnIndex(ConstSQLite.KEG_JENIS)));
                keg.setSubject(c.getString(c.getColumnIndex(ConstSQLite.KEG_SUBJECT)));
                keg.setKeterangan(c.getString(c.getColumnIndex(ConstSQLite.KEG_KETERANGAN)));
                keg.setResult(c.getString(c.getColumnIndex(ConstSQLite.KEG_RESULT)));
                keg.setCancel(c.getInt(c.getColumnIndex(ConstSQLite.KEG_CANCEL)));
                keg.setReason(c.getString(c.getColumnIndex(ConstSQLite.KEG_CANCEL_REASON)));
                keg.setStartDate(c.getString(c.getColumnIndex(ConstSQLite.KEG_START_DATE)));
                keg.setEndDate(c.getString(c.getColumnIndex(ConstSQLite.KEG_END_DATE)));
                keg.setCheckIn(c.getInt(c.getColumnIndex(ConstSQLite.KEG_CHECK_IN)));

                keg.setSalesHeader(sales_header);
                keg.setBEX(Global.getBEX(context));

            } catch (Exception e) {
                // TODO: handle exception
                query = e.toString();
            }
        } else keg = null;
        db.close();
        c.close();
        return keg;
    }

    public ArrayList<Integer> getOpen(String bex_no) {
        db = dbHelper.getReadableDatabase();
        ArrayList<Integer> list = new ArrayList<Integer>();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN + " K LEFT JOIN " +
                ConstSQLite.TABLE_HEADER + " H ON K." + ConstSQLite.KEG_ID + "=H." + ConstSQLite.KEY_HEADER_ID_KEGIATAN +
                " WHERE K." + ConstSQLite.KEY_BEX_NO + "='" + bex_no + "'" +
                " AND " + ConstSQLite.KEY_HEADER_STATUS + "<> 1";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                try {
                    int id;
                    id = c.getInt(c.getColumnIndex(ConstSQLite.KEY_HEADER_ID_KEGIATAN));
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
        String query = "SELECT K." + ConstSQLite.KEG_CANCEL + " AS Cancel, H." + ConstSQLite.KEY_HEADER_STATUS + " AS Status" +
                " FROM " + ConstSQLite.TABLE_KEGIATAN + " K LEFT JOIN " +
                ConstSQLite.TABLE_HEADER + " H ON K." + ConstSQLite.KEG_ID + "=H." + ConstSQLite.KEY_HEADER_ID_KEGIATAN +
                " WHERE K." + ConstSQLite.KEG_ID + "='" + Integer.toString(id) + "'";

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
        String query = "SELECT " + ConstSQLite.KEY_HEADER_STATUS +
                " FROM " + ConstSQLite.TABLE_HEADER +
                " WHERE " + ConstSQLite.KEY_HEADER_ID_KEGIATAN + "=" + Integer.toString(idKegiatan);

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            try {
                if (c.getInt(c.getColumnIndex(ConstSQLite.KEY_HEADER_STATUS)) == 1) {
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

    public void CheckOutAll() {
        db = dbHelper.getWritableDatabase();
        String qry = "UPDATE " + ConstSQLite.TABLE_KEGIATAN +
                " SET " + ConstSQLite.KEG_CHECK_IN + " = 0 ";
        db.execSQL(qry);
        db.close();
    }

    public void setStatus(ArrayList<Kegiatan> ks) {
        for (int i = 0; i < ks.size(); i++) {
            Kegiatan k = get(ks.get(i).getID());
            if (k != null) {
                k.setCancel(ks.get(i).getCancel());
                if (!ks.get(i).getSalesHeader().getOrderNo().equals("null"))
                    k.getSalesHeader().setOrderNo(ks.get(i).getSalesHeader().getOrderNo());
                if (ks.get(i).getSalesHeader().getHeaderID() != 0)
                    k.getSalesHeader().setHeaderID(ks.get(i).getSalesHeader().getHeaderID());
                if (k.getCancel() == 1)
                    k.setReason(ks.get(i).getReason());
                k.getSalesHeader().setStatus(ks.get(i).getSalesHeader().getStatus());
                Post(k);
            }
        }
    }

    public int getCurrentCheckIn() {
        db = dbHelper.getReadableDatabase();
        boolean result = false;
        String query = "SELECT * FROM " + ConstSQLite.TABLE_KEGIATAN +
                " WHERE " + ConstSQLite.KEY_BEX_NO + " = " + Global.getBEX(context).getEmpNo() +
                " AND " + ConstSQLite.KEY_BEX_INITIAL + " = '" + Global.getBEX(context).getInitial() + "'" +
                " AND " + ConstSQLite.KEG_CHECK_IN + " = 1";

        Cursor c = db.rawQuery(query, null);
        int id = 0;
        if (c.moveToFirst()) {
            try {
                id = c.getInt(c.getColumnIndex(ConstSQLite.KEG_ID));
            } catch (Exception e) {
                // TODO: handle exception
                query = e.toString();
            }
        } else result = false;
        db.close();
        c.close();
        return id;
    }

    public void setCheckIn(Kegiatan kegiatan) {
        kegiatan.setCheckIn(Const.CHECK_IN);
        Update(kegiatan);
    }
}
