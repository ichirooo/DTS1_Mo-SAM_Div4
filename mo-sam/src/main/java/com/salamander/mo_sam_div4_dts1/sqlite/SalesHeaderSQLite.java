package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.object.SalesHeader;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_base_module.object.Tanggal;

import java.util.ArrayList;

public class SalesHeaderSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public SalesHeaderSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public SalesHeader insert(SalesHeader salesHeader) {
        try {
            if (salesHeader.getIDServer() == 0)
                salesHeader.setIDServer(getTempID());
            db = dbHelper.getWritableDatabase();
            db.insert(ConstSQLite.TABLE_HEADER, null, salesHeaderToContentValues(salesHeader));
            db.close();

            return getFromKegiatan(salesHeader.getIDKegiatan());
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return salesHeader;
        }
    }

    public SalesHeader update(SalesHeader salesHeader) {
        try {
            db = dbHelper.getWritableDatabase();
            db.update(ConstSQLite.TABLE_HEADER, salesHeaderToContentValues(salesHeader), SalesHeader.SALES_HEADER_ID_SERVER + " = ?",
                    new String[]{String.valueOf(salesHeader.getIDServer())});
            db.close();

            return getFromKegiatan(salesHeader.getIDKegiatan());
        } catch (Exception e) {
            // TODO: handle exception
            db.close();
            return salesHeader;
        }
    }

    public SalesHeader updateID(int id_kegiatan, int oldID, int newID) {
        db = dbHelper.getWritableDatabase();
        ItemSQLite itemSQLite = new ItemSQLite(context);
        String sql = "UPDATE " + ConstSQLite.TABLE_HEADER +
                " SET " + SalesHeader.SALES_HEADER_ID_SERVER + " = " + String.valueOf(newID) +
                " WHERE " + SalesHeader.SALES_HEADER_ID_SERVER + " = " + String.valueOf(oldID);
        db.execSQL(sql);
        itemSQLite.updateIDHeader(oldID, newID);
        return getFromKegiatan(id_kegiatan);
    }

    public SalesHeader updateStatus(int id_kegiatan, int ID, int status) {
        db = dbHelper.getWritableDatabase();
        String sql = "UPDATE " + ConstSQLite.TABLE_HEADER +
                " SET " + SalesHeader.SALES_HEADER_STATUS + " = " + String.valueOf(status) +
                " WHERE " + SalesHeader.SALES_HEADER_ID_SERVER + " = " + String.valueOf(ID);
        db.execSQL(sql);
        return getFromKegiatan(id_kegiatan);
    }

    public SalesHeader post(SalesHeader salesHeader) {
        new CustomerSQLite(context).Post(salesHeader.getCustomer());
        ArrayList<Item> sales_line = salesHeader.getSalesLine();
        if (!Utils.isEmpty(salesHeader.getOrderNo())) {
            if (!isExist(salesHeader))
                salesHeader = insert(salesHeader);
            else
                salesHeader = update(salesHeader);
        }
        ItemSQLite itemSQLite = new ItemSQLite(context);
        itemSQLite.deleteByHeader(salesHeader.getIDServer());
        for (Item item : sales_line) {
            item.setIDHeader(salesHeader.getIDServer());
            itemSQLite.post(item);
        }
        return getFromKegiatan(salesHeader.getIDKegiatan());
    }

    private boolean isExist(SalesHeader salesHeader) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_HEADER + " WHERE " +
                SalesHeader.SALES_HEADER_ID_SERVER + " = " + Integer.toString(salesHeader.getIDServer());
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        c.close();
        db.close();
        return exist;
    }

    public SalesHeader getFromKegiatan(int id_kegiatan) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_HEADER + " WHERE " +
                SalesHeader.SALES_HEADER_ID_KEGIATAN + " = " + id_kegiatan;
        SalesHeader salesHeader = new SalesHeader();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            salesHeader = cursorToSalesHeader(c);
            db.close();
        }
        if (salesHeader.getCustomer() == null || Utils.isEmpty(salesHeader.getCustomer().getCode()))
            salesHeader.setCustomer(new CustomerSQLite(context).get(id_kegiatan));
        if (salesHeader.getSalesperson() == null || salesHeader.getSalesperson().getEmpNo() == 0)
            salesHeader.setSalesperson(App.getUser(context));
        db.close();
        c.close();
        return salesHeader;
    }

    public String getTempOrderNo() {
        String order_no = "XXX-001";
        db = dbHelper.getReadableDatabase();
        String query = "SELECT " + SalesHeader.SALES_HEADER_ORDER_NO + " FROM " + ConstSQLite.TABLE_HEADER + " WHERE " +
                SalesHeader.SALES_HEADER_ORDER_NO + " LIKE 'XXX-%'" +
                " ORDER BY " + SalesHeader.SALES_HEADER_ORDER_NO + " DESC" +
                " LIMIT 1";
        String no = "XXX-001";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            try {
                no = c.getString(c.getColumnIndex(SalesHeader.SALES_HEADER_ORDER_NO));
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

    public int getTempID() {
        int id = ConstSQLite.TEMP_ID_SALES_HEADER + 1;
        db = dbHelper.getReadableDatabase();
        String query = "SELECT MAX(" + SalesHeader.SALES_HEADER_ID_SERVER + ") AS " + SalesHeader.SALES_HEADER_ID_SERVER + " FROM " + ConstSQLite.TABLE_HEADER + " WHERE " +
                SalesHeader.SALES_HEADER_ID_SERVER + " > " + String.valueOf(ConstSQLite.TEMP_ID_SALES_HEADER);
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            try {
                id = c.getInt(c.getColumnIndex(SalesHeader.SALES_HEADER_ID_SERVER));
                if (id > ConstSQLite.TEMP_ID_SALES_HEADER)
                    id += 1;
                else id = ConstSQLite.TEMP_ID_SALES_HEADER + 1;
            } catch (Exception e) {
                //FileUtil.writeExceptionLog(context, SalesHeaderSQLite.class.getSimpleName() + " => getTempID => ", e);
                Log.d(App.TAG, SalesHeaderSQLite.class.getSimpleName() + " => getTempID  => " + e.toString());
            }
        }
        db.close();
        c.close();
        return id;
    }

    /*
    private int getTempID() {
        db = dbHelper.getReadableDatabase();
        String query = " SELECT " + SalesHeader.SALES_HEADER_ID_SERVER +
                " FROM " + ConstSQLite.TABLE_HEADER +
                " WHERE " + SalesHeader.SALES_HEADER_ORDER_NO + " LIKE 'XXX-%' " +
                " AND " + SalesHeader.SALES_HEADER_ID_SERVER + " > " + String.valueOf(Const.DEFAULT_HEADER_ID) +
                " ORDER BY " + SalesHeader.SALES_HEADER_ID_SERVER + " DESC " +
                " LIMIT 1";

        Cursor c = db.rawQuery(query, null);
        int no = Const.DEFAULT_HEADER_ID + 1;
        if (c.moveToFirst()) {
            try {
                no = c.getInt(c.getColumnIndex(SalesHeader.SALES_HEADER_ID_SERVER));
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
    */

    public void delete(int idHeader) {
        new ItemSQLite(context).deleteFromSalesHeader(idHeader);
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_HEADER +
                " WHERE " + SalesHeader.SALES_HEADER_ID_SERVER + " = " + String.valueOf(idHeader);
        db.execSQL(query);
        db.close();
    }

    public void deleteFromKegiatan(int id_kegiatan) {
        SalesHeader salesHeader = getFromKegiatan(id_kegiatan);
        new ItemSQLite(context).deleteFromSalesHeader(salesHeader.getIDServer());
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_HEADER +
                " WHERE " + SalesHeader.SALES_HEADER_ID_KEGIATAN + " = " + String.valueOf(id_kegiatan);
        db.execSQL(query);
        db.close();
    }

    private ContentValues salesHeaderToContentValues(SalesHeader salesHeader) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SalesHeader.SALES_HEADER_ID_SERVER, salesHeader.getIDServer());
        contentValues.put(SalesHeader.SALES_HEADER_ID_KEGIATAN, salesHeader.getIDKegiatan());
        contentValues.put(SalesHeader.SALES_HEADER_ORDER_NO, salesHeader.getOrderNo());
        contentValues.put(SalesHeader.SALES_HEADER_SYARAT_PEMBAYARAN, salesHeader.getSyarat());
        contentValues.put(SalesHeader.SALES_HEADER_TEMPO, salesHeader.getTempo());
        contentValues.put(SalesHeader.SALES_HEADER_CURRENCY, salesHeader.getCurrency());
        contentValues.put(SalesHeader.SALES_HEADER_NOTES, salesHeader.getNotes());
        contentValues.put(SalesHeader.SALES_HEADER_STATUS, salesHeader.getStatus());
        contentValues.put(SalesHeader.SALES_HEADER_PROMISED_DELIVERY_DATE, salesHeader.getPromisedDate().getTglString());
        contentValues.put(SalesHeader.SALES_HEADER_SUBTOTAL, salesHeader.getSubtotal());
        contentValues.put(SalesHeader.SALES_HEADER_POTONGAN, salesHeader.getPotongan());
        contentValues.put(SalesHeader.SALES_HEADER_DPP, salesHeader.getDPP());
        contentValues.put(SalesHeader.SALES_HEADER_DPP_PPN, salesHeader.getPPnDPP());
        contentValues.put(SalesHeader.SALES_HEADER_TOTAL, salesHeader.getTotal());
        contentValues.put(SalesHeader.SALES_HEADER_INPUT_DATE, salesHeader.getInputDate().getTglString());
        return contentValues;
    }

    private SalesHeader cursorToSalesHeader(Cursor cursor) {
        SalesHeader salesHeader = new SalesHeader();
        salesHeader.setIDServer(ConstSQLite.getInt(cursor, SalesHeader.SALES_HEADER_ID_SERVER));
        salesHeader.setIDLocal(ConstSQLite.getInt(cursor, SalesHeader.SALES_HEADER_ID_LOCAL));
        salesHeader.setIDKegiatan(ConstSQLite.getInt(cursor, SalesHeader.SALES_HEADER_ID_KEGIATAN));
        salesHeader.setOrderNo(ConstSQLite.getString(cursor, SalesHeader.SALES_HEADER_ORDER_NO));
        salesHeader.setSyarat(ConstSQLite.getString(cursor, SalesHeader.SALES_HEADER_SYARAT_PEMBAYARAN));
        salesHeader.setTempo(ConstSQLite.getString(cursor, SalesHeader.SALES_HEADER_TEMPO));
        salesHeader.setCurrency(ConstSQLite.getString(cursor, SalesHeader.SALES_HEADER_CURRENCY));
        salesHeader.setStatus(ConstSQLite.getInt(cursor, SalesHeader.SALES_HEADER_STATUS));
        salesHeader.setSubtotal(ConstSQLite.getDouble(cursor, SalesHeader.SALES_HEADER_SUBTOTAL));
        salesHeader.setPotongan(ConstSQLite.getDouble(cursor, SalesHeader.SALES_HEADER_POTONGAN));
        salesHeader.setDPP(ConstSQLite.getDouble(cursor, SalesHeader.SALES_HEADER_DPP));
        salesHeader.setPPnDPP(ConstSQLite.getDouble(cursor, SalesHeader.SALES_HEADER_DPP_PPN));
        salesHeader.setTotal(ConstSQLite.getDouble(cursor, SalesHeader.SALES_HEADER_TOTAL));
        salesHeader.setNotes(ConstSQLite.getString(cursor, SalesHeader.SALES_HEADER_NOTES));
        salesHeader.setPromisedDate(new Tanggal(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, ConstSQLite.getString(cursor, SalesHeader.SALES_HEADER_PROMISED_DELIVERY_DATE))));
        salesHeader.setInputDate(new Tanggal(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, ConstSQLite.getString(cursor, SalesHeader.SALES_HEADER_INPUT_DATE))));
        ArrayList<Item> salesLines = new ItemSQLite(context).getFromSalesHeader(salesHeader.getIDServer());
        salesHeader.setSalesLine(salesLines);
        return salesHeader;
    }
}
