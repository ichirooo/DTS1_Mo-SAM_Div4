package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.SalesHeader;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;

import java.util.ArrayList;

import static com.salamander.mo_sam_div4_dts1.sqlite.ConstSQLite.LINE_NO_INCREMENT;

public class ItemSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ItemSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    private Item insert(Item item) {
        try {
            db = dbHelper.getWritableDatabase();
            item.setLineNo(getNextLineNo(item.getIDHeader()));
            long i = db.insertOrThrow(ConstSQLite.TABLE_LINE, null, itemToContentValues(item));
            db.close();
            return get(item.getIDHeader(), item.getLineNo());
        } catch (SQLException e) {
            // TODO: handle exception
            db.close();
            return item;
        }
    }

    public Item update(Item item) {
        try {
            db = dbHelper.getWritableDatabase();
            long i = db.update(ConstSQLite.TABLE_LINE, itemToContentValues(item), Item.ITEM_ID_SERVER + " = ? ",
                    new String[]{String.valueOf(item.getIDServer())});
            db.close();
            return get(item.getIDHeader(), item.getLineNo());
        } catch (SQLException e) {
            // TODO: handle exception
            db.close();
            return item;
        }
    }

    public void updateIDHeader(int oldID, int newID) {
        db = dbHelper.getWritableDatabase();
        String sql = "UPDATE " + ConstSQLite.TABLE_LINE +
                " SET " + Item.ITEM_ID_HEADER + " = " + String.valueOf(newID) +
                " WHERE " + Item.ITEM_ID_HEADER + " = " + String.valueOf(oldID);
        db.execSQL(sql);
    }

    public Item post(Item item) {
        if (!isExist(item))
            return insert(item);
        else return update(item);
    }

    public ArrayList<Item> Post(ArrayList<Item> items) {
        deleteUnusedItem(items);
        ArrayList<Item> list_item = new ArrayList<>();
        for (Item item : items) {
            list_item.add(post(item));
        }
        return list_item;
    }

    private boolean isExist(Item item) {
        db = dbHelper.getReadableDatabase();
        boolean isExist;
        String query = "SELECT * FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + Item.ITEM_ID_SERVER + " = " + String.valueOf(item.getIDServer()) +
                " AND " + Item.ITEM_LINE_NO + " = " + String.valueOf(item.getLineNo());
        Cursor cursor = db.rawQuery(query, null);
        isExist = cursor.getCount() > 0;
        db.close();
        cursor.close();
        return isExist;
    }

    public void deleteByHeader(int headerID) {
        db = dbHelper.getReadableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + Item.ITEM_ID_HEADER + " = " + String.valueOf(headerID);
        db.execSQL(query);
        db.close();
    }

    private void deleteUnusedItem(ArrayList<Item> items) {
        db = dbHelper.getWritableDatabase();
        int header_id = 0;
        if (items.size() > 0)
            header_id = items.get(0).getIDHeader();
        String item_id = "";
        for (int i = 0; i < items.size(); i++) {
            if (i == items.size() - 1)
                item_id = item_id + Integer.toString(items.get(i).getIDServer());
            else
                item_id = item_id + Integer.toString(items.get(i).getIDServer()) + ",";
        }
        String query = " DELETE FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + Item.ITEM_ID_HEADER + " = " + Integer.toString(header_id) +
                " AND " + Item.ITEM_LINE_NO + " NOT IN (" + item_id + ")";
        db.execSQL(query);
        db.close();
    }

    public ArrayList<Item> getFromSalesHeader(int headerID) {
        db = dbHelper.getReadableDatabase();
        ArrayList<Item> listitem = new ArrayList<Item>();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_LINE
                + " WHERE " + Item.ITEM_ID_HEADER + " = " + Integer.toString(headerID);
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                listitem.add(cursorToItem(c));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return listitem;
    }

    public Item get(int id_header, int line_no) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_LINE + " WHERE " +
                Item.ITEM_ID_HEADER + " = " + String.valueOf(id_header) + " AND " +
                Item.ITEM_LINE_NO + " = " + String.valueOf(line_no);
        Cursor c = db.rawQuery(query, null);
        Item item = null;
        if (c.moveToFirst()) {
            item = cursorToItem(c);
        }
        db.close();
        c.close();
        return item;
    }

    public boolean isError(int Header_ID) {
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + ConstSQLite.TABLE_LINE + " WHERE " +
                Item.ITEM_ID_HEADER + " = " + String.valueOf(Header_ID) + " AND " +
                Item.ITEM_IS_ERROR + " = 1";
        Cursor c = db.rawQuery(sql, null);
        boolean error = c.getCount() > 0;
        db.close();
        c.close();
        return error;
    }

    public ArrayList<String> getErrorAndroSO(User user) {
        db = dbHelper.getReadableDatabase();
        ArrayList<String> sos = new ArrayList<>();
        String sql = "SELECT DISTINCT " + SalesHeader.SALES_HEADER_ORDER_NO + " FROM " + ConstSQLite.TABLE_HEADER +
                " AS H JOIN " + ConstSQLite.TABLE_LINE + " AS L " +
                " ON H." + SalesHeader.SALES_HEADER_ID_SERVER + " = L." + Item.ITEM_ID_HEADER +
                " WHERE " + SalesHeader.SALES_HEADER_ID_KEGIATAN + " IN (" +
                "SELECT " + Kegiatan.KEGIATAN_ID_SERVER + " FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                User.BEX_EMP_NO + " = " + user.getEmpNo() + ") " +
                " AND " + Item.ITEM_IS_ERROR + " = 1 " +
                " ORDER BY " + Item.ITEM_IS_ERROR;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                sos.add(c.getString(c.getColumnIndex(SalesHeader.SALES_HEADER_ORDER_NO)));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return sos;
    }

    public ArrayList<String> getItemError(String andro_so) {
        db = dbHelper.getReadableDatabase();
        ArrayList<String> sos = new ArrayList<>();
        String sql = "SELECT DISTINCT " + Item.ITEM_CODE + " FROM " + ConstSQLite.TABLE_LINE +
                " AS L JOIN " + ConstSQLite.TABLE_HEADER + " AS H " +
                " ON L." + Item.ITEM_ID_HEADER + " = H." + SalesHeader.SALES_HEADER_ID_SERVER +
                " WHERE " + SalesHeader.SALES_HEADER_ORDER_NO + " = '" + andro_so + "'" +
                " AND " + Item.ITEM_IS_ERROR + " = 1 " +
                " ORDER BY " + Item.ITEM_CODE;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                sos.add(c.getString(c.getColumnIndex(Item.ITEM_CODE)));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return sos;
    }

    public void deleteItem(int line_id) {
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + Item.ITEM_ID_SERVER + " = " + String.valueOf(line_id);
        db.execSQL(query);
        db.close();
    }

    public void deleteFromSalesHeader(int idHeader) {
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + Item.ITEM_ID_HEADER + " = " + String.valueOf(idHeader);
        db.execSQL(query);
        db.close();
    }

    public ArrayList<Item> Find(String teks) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT  T1." + Item.ITEM_CODE + "," +
                Item.ITEM_DESCRIPTION + ", " +
                Item.ITEM_UNIT + ", " +
                "T1." + Item.ITEM_PRICE + "," +
                "T2.Last_Date AS " + Item.ITEM_INPUT_DATE +
                " FROM " + ConstSQLite.TABLE_LINE + " AS T1 " +
                "INNER JOIN (SELECT " +
                Item.ITEM_CODE + ", " +
                "MAX(" + Item.ITEM_INPUT_DATE + ") AS Last_Date " +
                " FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + Item.ITEM_PRICE + " <> 0" +
                " GROUP BY " + Item.ITEM_CODE + ") AS T2 " +
                " ON T1." + Item.ITEM_CODE + " = T2." + Item.ITEM_CODE +
                " AND T1." + Item.ITEM_INPUT_DATE + " = T2.Last_Date " +
                "WHERE " +
                "T1." + Item.ITEM_CODE + " LIKE '%" + teks + "%' OR " +
                "T1." + Item.ITEM_DESCRIPTION + " LIKE '%" + teks + "%' LIMIT 10";

        ArrayList<Item> list_item = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list_item.add(cursorToItem(c));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_item;
    }

    private int getNextLineNo(int id_sales_header) {
        db = dbHelper.getReadableDatabase();
        int LineNo = 0;
        String sql = "SELECT MAX(" + Item.ITEM_LINE_NO + ") AS " + Item.ITEM_LINE_NO +
                " FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + Item.ITEM_ID_HEADER + " = " + String.valueOf(id_sales_header);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst())
            LineNo = ConstSQLite.getInt(cursor, Item.ITEM_LINE_NO);
        return LineNo + LINE_NO_INCREMENT;
    }

    private ContentValues itemToContentValues(Item item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Item.ITEM_ID_HEADER, item.getIDHeader());
        contentValues.put(Item.ITEM_ORDER_NO, item.getOrderNo());
        contentValues.put(Item.ITEM_ID_SERVER, item.getIDServer());
        contentValues.put(Item.ITEM_LINE_NO, item.getLineNo());
        contentValues.put(Item.ITEM_CODE, item.getCode());
        contentValues.put(Item.ITEM_DESCRIPTION, item.getDescription());
        contentValues.put(Item.ITEM_UNIT, item.getUnit());
        contentValues.put(Item.ITEM_QUANTITY, item.getQuantity());
        contentValues.put(Item.ITEM_PRICE, item.getPrice());
        contentValues.put(Item.ITEM_PRICE_NAV, item.getPriceNAV());
        contentValues.put(Item.ITEM_DISCOUNT_PCT, item.getDiscountPct());
        contentValues.put(Item.ITEM_DISCOUNT_VALUE, item.getDiscountValue());
        contentValues.put(Item.ITEM_SUBTOTAL, item.getSubtotal());
        contentValues.put(Item.ITEM_IS_ERROR, item.getError());
        contentValues.put(Item.ITEM_NOTES, item.getNotes());
        if (item.getInputDate().getDate().getTime() != 0)
            contentValues.put(Item.ITEM_INPUT_DATE, item.getInputDate().getTglString());
        return contentValues;
    }

    private Item cursorToItem(Cursor cursor) {
        Item item = new Item();
        item.setIDHeader(ConstSQLite.getInt(cursor, Item.ITEM_ID_HEADER));
        item.setOrderNo(ConstSQLite.getString(cursor, Item.ITEM_ORDER_NO));
        item.setIDServer(ConstSQLite.getInt(cursor, Item.ITEM_ID_HEADER));
        item.setLineNo(ConstSQLite.getInt(cursor, Item.ITEM_LINE_NO));
        item.setCode(ConstSQLite.getString(cursor, Item.ITEM_CODE));
        item.setDescription(ConstSQLite.getString(cursor, Item.ITEM_DESCRIPTION));
        item.setUnit(ConstSQLite.getString(cursor, Item.ITEM_UNIT));
        item.setQuantity(ConstSQLite.getInt(cursor, Item.ITEM_QUANTITY));
        item.setPrice(ConstSQLite.getInt(cursor, Item.ITEM_PRICE));
        item.setPriceNAV(ConstSQLite.getInt(cursor, Item.ITEM_PRICE_NAV));
        item.setDiscountValue(ConstSQLite.getInt(cursor, Item.ITEM_DISCOUNT_VALUE));
        item.setDiscountPct(ConstSQLite.getInt(cursor, Item.ITEM_DISCOUNT_PCT));
        item.setSubtotal(ConstSQLite.getInt(cursor, Item.ITEM_SUBTOTAL));
        item.setError(ConstSQLite.getInt(cursor, Item.ITEM_IS_ERROR));
        item.setNotes(ConstSQLite.getString(cursor, Item.ITEM_NOTES));
        item.setInputDate(new Tanggal(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, ConstSQLite.getString(cursor, Item.ITEM_INPUT_DATE))));
        return item;
    }
}
