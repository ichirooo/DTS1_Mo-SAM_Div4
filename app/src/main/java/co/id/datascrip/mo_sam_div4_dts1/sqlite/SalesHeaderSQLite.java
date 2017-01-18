package co.id.datascrip.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.Const;
import co.id.datascrip.mo_sam_div4_dts1.function.Waktu;
import co.id.datascrip.mo_sam_div4_dts1.object.Customer;
import co.id.datascrip.mo_sam_div4_dts1.object.Item;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.object.SalesHeader;

public class SalesHeaderSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public SalesHeaderSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public boolean Insert(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEY_HEADER_ID_KEGIATAN, kegiatan.getID());
            values.put(ConstSQLite.KEY_HEADER_ID, kegiatan.getSalesHeader().getHeaderID());
            values.put(ConstSQLite.KEY_HEADER_NO_ORDER, kegiatan.getSalesHeader().getOrderNo());
            values.put(ConstSQLite.KEY_HEADER_CURRENCY, kegiatan.getSalesHeader().getCurrency());
            values.put(ConstSQLite.KEY_HEADER_SYARAT, kegiatan.getSalesHeader().getSyarat());
            values.put(ConstSQLite.KEY_HEADER_TEMPO, kegiatan.getSalesHeader().getTempo());
            values.put(ConstSQLite.KEY_HEADER_STATUS, kegiatan.getSalesHeader().getStatus());
            values.put(ConstSQLite.KEY_HEADER_NOTES, kegiatan.getSalesHeader().getNotes());
            values.put(ConstSQLite.KEY_HEADER_PROMISED_DATE, kegiatan.getSalesHeader().getPromisedDate());
            values.put(ConstSQLite.KEY_TOTAL_SUBTOTAL, kegiatan.getSalesHeader().getSubtotal());
            values.put(ConstSQLite.KEY_TOTAL_GRANDTOTAL, kegiatan.getSalesHeader().getTotal());
            values.put(ConstSQLite.KEY_TOTAL_DISCOUNT, kegiatan.getSalesHeader().getPotongan());
            values.put(ConstSQLite.KEY_TOTAL_DPP, kegiatan.getSalesHeader().getDPP());
            values.put(ConstSQLite.KEY_TOTAL_TAX, kegiatan.getSalesHeader().getPPnDPP());
            values.put(ConstSQLite.INPUT_DATE, new Waktu().getCurrent());

            db.insert(ConstSQLite.TABLE_HEADER, null, values);
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
            values.put(ConstSQLite.KEY_HEADER_ID_KEGIATAN, kegiatan.getID());
            values.put(ConstSQLite.KEY_HEADER_ID, kegiatan.getSalesHeader().getHeaderID());
            values.put(ConstSQLite.KEY_HEADER_NO_ORDER, kegiatan.getSalesHeader().getOrderNo());
            values.put(ConstSQLite.KEY_HEADER_CURRENCY, kegiatan.getSalesHeader().getCurrency());
            values.put(ConstSQLite.KEY_HEADER_SYARAT, kegiatan.getSalesHeader().getSyarat());
            values.put(ConstSQLite.KEY_HEADER_TEMPO, kegiatan.getSalesHeader().getTempo());
            values.put(ConstSQLite.KEY_HEADER_STATUS, kegiatan.getSalesHeader().getStatus());
            values.put(ConstSQLite.KEY_HEADER_NOTES, kegiatan.getSalesHeader().getNotes());
            values.put(ConstSQLite.KEY_HEADER_PROMISED_DATE, kegiatan.getSalesHeader().getPromisedDate());
            values.put(ConstSQLite.KEY_TOTAL_SUBTOTAL, kegiatan.getSalesHeader().getSubtotal());
            values.put(ConstSQLite.KEY_TOTAL_DISCOUNT, kegiatan.getSalesHeader().getPotongan());
            values.put(ConstSQLite.KEY_TOTAL_DPP, kegiatan.getSalesHeader().getDPP());
            values.put(ConstSQLite.KEY_TOTAL_TAX, kegiatan.getSalesHeader().getPPnDPP());
            values.put(ConstSQLite.INPUT_DATE, new Waktu().getCurrent());

            db.update(ConstSQLite.TABLE_HEADER, values, ConstSQLite.KEY_HEADER_ID_KEGIATAN + " = ?",
                    new String[]{String.valueOf(kegiatan.getID())});
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return false;
        }
    }

    public boolean Post(Kegiatan kegiatan) {
        new CustomerSQLite(context).Post(kegiatan);
        if (isExist(kegiatan)) {
            if (Update(kegiatan)) {
                for (int i = 0; i < kegiatan.getSalesHeader().getSalesLine().size(); i++)
                    kegiatan.getSalesHeader().getSalesLine().get(i).setHeaderID(kegiatan.getSalesHeader().getHeaderID());
                new ItemSQLite(context).Post(kegiatan.getSalesHeader().getSalesLine());
            } else
                return false;
        } else {
            if (kegiatan.getSalesHeader().getHeaderID() == 0) {
                kegiatan.getSalesHeader().setHeaderID(getTempID());
            }
            if (kegiatan.getSalesHeader().getOrderNo() != null) {
                if (Insert(kegiatan)) {
                    for (int i = 0; i < kegiatan.getSalesHeader().getSalesLine().size(); i++) {
                        if (kegiatan.getSalesHeader().getSalesLine().get(i).getHeaderID() == 0)
                            kegiatan.getSalesHeader().getSalesLine().get(i).setHeaderID(kegiatan.getSalesHeader().getHeaderID());
                    }
                    new ItemSQLite(context).Post(kegiatan.getSalesHeader().getSalesLine());
                } else
                    return false;
            }
        }
        return true;
    }

    private boolean isExist(Kegiatan kegiatan) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_HEADER + " WHERE " +
                ConstSQLite.KEY_HEADER_ID_KEGIATAN + " = " + Integer.toString(kegiatan.getID());
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        db.close();
        return exist;
    }

    public SalesHeader get(int kegid) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_HEADER + " WHERE " +
                ConstSQLite.KEY_HEADER_ID_KEGIATAN + " = " + kegid;
        SalesHeader header = new SalesHeader();
        Customer customer = new CustomerSQLite(context).get(kegid);
        header.setCustomer(customer);
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            try {
                header.setHeaderID(c.getInt(c.getColumnIndex(ConstSQLite.KEY_HEADER_ID)));
                header.setOrderNo(c.getString(c.getColumnIndex(ConstSQLite.KEY_HEADER_NO_ORDER)));
                header.setSyarat(c.getString(c.getColumnIndex(ConstSQLite.KEY_HEADER_SYARAT)));
                header.setTempo(c.getString(c.getColumnIndex(ConstSQLite.KEY_HEADER_TEMPO)));
                header.setCurrency(c.getString(c.getColumnIndex(ConstSQLite.KEY_HEADER_CURRENCY)));
                header.setStatus(c.getInt(c.getColumnIndex(ConstSQLite.KEY_HEADER_STATUS)));
                header.setPromisedDate(c.getString(c.getColumnIndex(ConstSQLite.KEY_HEADER_PROMISED_DATE)));
                header.setSubtotal(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_TOTAL_SUBTOTAL)));
                header.setPotongan(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_TOTAL_DISCOUNT)));
                header.setDPP(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_TOTAL_DPP)));
                header.setPPnDPP(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_TOTAL_TAX)));
                header.setTotal(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_TOTAL_GRANDTOTAL)));
                header.setNotes(c.getString(c.getColumnIndex(ConstSQLite.KEY_HEADER_NOTES)));

                ArrayList<Item> lines = new ItemSQLite(context).get(header.getHeaderID());

                header.setSalesLine(lines);

            } catch (Exception e) {
                // TODO: handle exception
                query = e.toString();
            }
            db.close();
        }
        db.close();
        c.close();
        return header;
    }

    public String getTempOrderNo() {
        String order_no = "XXX-001";
        db = dbHelper.getReadableDatabase();
        String query = "SELECT " + ConstSQLite.KEY_HEADER_NO_ORDER + " FROM " + ConstSQLite.TABLE_HEADER + " WHERE " +
                ConstSQLite.KEY_HEADER_NO_ORDER + " LIKE 'XXX-%'" +
                " ORDER BY " + ConstSQLite.KEY_HEADER_NO_ORDER + " DESC" +
                " LIMIT 1";
        String no = "XXX-001";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            try {
                no = c.getString(c.getColumnIndex(ConstSQLite.KEY_HEADER_NO_ORDER));
                int no_o = Integer.valueOf(no.substring(5)) + 1;
                no = String.valueOf(no_o);
                for (int i = 3; i > 1; i--)
                    no = "0" + no;
                order_no = "XXX-" + no;
            } catch (Exception e) {
                // TODO: handle exception
                query = e.toString();
            }
            db.close();
        }
        db.close();
        c.close();
        return order_no;
    }

    private int getTempID() {
        db = dbHelper.getReadableDatabase();
        String query = " SELECT " + ConstSQLite.KEY_HEADER_ID +
                " FROM " + ConstSQLite.TABLE_HEADER +
                " WHERE " + ConstSQLite.KEY_HEADER_NO_ORDER + " LIKE 'XXX-%' " +
                " AND " + ConstSQLite.KEY_HEADER_ID + " > " + String.valueOf(Const.DEFAULT_HEADER_ID) +
                " ORDER BY " + ConstSQLite.KEY_HEADER_ID + " DESC " +
                " LIMIT 1";

        Cursor c = db.rawQuery(query, null);
        int no = Const.DEFAULT_HEADER_ID + 1;
        if (c.moveToFirst()) {
            try {
                no = c.getInt(c.getColumnIndex(ConstSQLite.KEY_HEADER_ID));
                no = no + 1;
            } catch (Exception e) {
                // TODO: handle exception
                query = e.toString();
            }
            db.close();
        }
        db.close();
        c.close();
        return no;
    }

    public void Delete(int id_kegiatan) {
        SalesHeader h = get(id_kegiatan);
        for (int i = 0; i < h.getSalesLine().size(); i++)
            new ItemSQLite(context).Delete(h.getSalesLine().get(i).getID());
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_HEADER +
                " WHERE " + ConstSQLite.KEY_HEADER_ID_KEGIATAN + " = " + String.valueOf(id_kegiatan);
        db.execSQL(query);
        db.close();
    }
}
