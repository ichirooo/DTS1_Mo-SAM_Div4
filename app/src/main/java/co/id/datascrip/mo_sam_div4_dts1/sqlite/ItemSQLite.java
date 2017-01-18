package co.id.datascrip.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.function.Waktu;
import co.id.datascrip.mo_sam_div4_dts1.object.BEX;
import co.id.datascrip.mo_sam_div4_dts1.object.Item;

public class ItemSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ItemSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    int Insert(Item item) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEY_LINE_HEADER_ID, item.getHeaderID());
            values.put(ConstSQLite.KEY_LINE_ITEM_CODE, item.getCode());
            values.put(ConstSQLite.KEY_LINE_ITEM_DESC, item.getDesc());
            values.put(ConstSQLite.KEY_LINE_ITEM_PRICE, item.getPrice());
            values.put(ConstSQLite.KEY_LINE_ITEM_DISC, item.getDiscount());
            values.put(ConstSQLite.KEY_LINE_ITEM_DISC_PCT, item.getDiscPct());
            values.put(ConstSQLite.KEY_LINE_ITEM_QTY, item.getQuantity());
            values.put(ConstSQLite.KEY_LINE_ITEM_UNIT, item.getUnit());
            values.put(ConstSQLite.KEY_LINE_ITEM_SUBTOTAL, item.getSubtotal());
            values.put(ConstSQLite.KEY_LINE_ERROR, item.getError());
            values.put(ConstSQLite.KEY_LINE_NOTES_FEEDBACK, item.getNotes());
            values.put(ConstSQLite.INPUT_DATE, new Waktu().getCurrent());

            long i = db.insert(ConstSQLite.TABLE_LINE, null, values);
            db.close();
            return (int) i;
        } catch (SQLException e) {
            // TODO: handle exception
            db.close();
            return 0;
        }
    }

    public int Update(Item item) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEY_LINE_HEADER_ID, item.getHeaderID());
            values.put(ConstSQLite.KEY_LINE_ITEM_CODE, item.getCode());
            values.put(ConstSQLite.KEY_LINE_ITEM_DESC, item.getDesc());
            values.put(ConstSQLite.KEY_LINE_ITEM_PRICE, item.getPrice());
            values.put(ConstSQLite.KEY_LINE_ITEM_DISC, item.getDiscount());
            values.put(ConstSQLite.KEY_LINE_ITEM_DISC_PCT, item.getDiscPct());
            values.put(ConstSQLite.KEY_LINE_ITEM_QTY, item.getQuantity());
            values.put(ConstSQLite.KEY_LINE_ITEM_UNIT, item.getUnit());
            values.put(ConstSQLite.KEY_LINE_ITEM_SUBTOTAL, item.getSubtotal());
            values.put(ConstSQLite.KEY_LINE_ERROR, item.getError());
            values.put(ConstSQLite.KEY_LINE_NOTES_FEEDBACK, item.getNotes());

            long i = db.update(ConstSQLite.TABLE_LINE, values, ConstSQLite.KEY_LINE_ID + " = ? ",
                    new String[]{String.valueOf(item.getID())});
            db.close();
            return item.getID();
        } catch (SQLException e) {
            // TODO: handle exception
            db.close();
            return 0;
        }
    }

    public int Post(Item item) {
        if (item.getID() == 0)
            return Insert(item);
        else
            return Update(item);
    }

    public ArrayList<Item> Post(ArrayList<Item> items) {
        deleteUnusedItem(items);
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setID(Post(items.get(i)));
        }
        return items;
    }

    private void deleteUnusedItem(ArrayList<Item> items) {
        db = dbHelper.getWritableDatabase();
        int header_id = 0;
        if (items.size() > 0)
            header_id = items.get(0).getHeaderID();
        String item_id = "";
        for (int i = 0; i < items.size(); i++) {
            if (i == items.size() - 1)
                item_id = item_id + Integer.toString(items.get(i).getID());
            else
                item_id = item_id + Integer.toString(items.get(i).getID()) + ",";
        }
        String query = " DELETE FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + ConstSQLite.KEY_LINE_HEADER_ID + " = " + Integer.toString(header_id) +
                " AND " + ConstSQLite.KEY_LINE_ID + " NOT IN (" + item_id + ")";
        db.execSQL(query);
        db.close();
    }

    public ArrayList<Item> get(int headerID) {
        db = dbHelper.getReadableDatabase();
        ArrayList<Item> listitem = new ArrayList<Item>();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_LINE
                + " WHERE " + ConstSQLite.KEY_LINE_HEADER_ID + " = " + Integer.toString(headerID);
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Item item = new Item();
                item.setID(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LINE_ID)));
                item.setHeaderID(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LINE_HEADER_ID)));
                item.setCode(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_CODE)));
                item.setDesc(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_DESC)));
                item.setPrice(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_PRICE)));
                item.setDiscount(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_DISC)));
                item.setDiscPct(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_DISC_PCT)));
                item.setQuantity(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_QTY)));
                item.setUnit(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_UNIT)));
                item.setSubtotal(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_SUBTOTAL)));
                item.setError(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LINE_ERROR)));
                item.setNotes(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_NOTES_FEEDBACK)));
                listitem.add(item);
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return listitem;
    }

    public Item getItem(int line_id) {
        db = dbHelper.getReadableDatabase();
        Item item = new Item();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_LINE + " WHERE " +
                ConstSQLite.KEY_LINE_ID + " = " + String.valueOf(line_id);
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            item.setID(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LINE_ID)));
            item.setHeaderID(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LINE_HEADER_ID)));
            item.setCode(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_CODE)));
            item.setDesc(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_DESC)));
            item.setPrice(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_PRICE)));
            item.setDiscount(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_DISC)));
            item.setDiscPct(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_DISC_PCT)));
            item.setQuantity(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_QTY)));
            item.setUnit(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_UNIT)));
            item.setSubtotal(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_SUBTOTAL)));
            item.setError(c.getInt(c.getColumnIndex(ConstSQLite.KEY_LINE_ERROR)));
            item.setNotes(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_NOTES_FEEDBACK)));
            db.close();
            c.close();
            return item;
        } else {
            db.close();
            c.close();
            return null;
        }
    }

    public boolean isError(int Header_ID) {
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + ConstSQLite.TABLE_LINE + " WHERE " +
                ConstSQLite.KEY_LINE_HEADER_ID + " = " + String.valueOf(Header_ID) + " AND " +
                ConstSQLite.KEY_LINE_ERROR + " = 1";
        Cursor c = db.rawQuery(sql, null);
        boolean error = c.getCount() > 0;
        db.close();
        c.close();
        return error;
    }

    public ArrayList<String> getErrorAndroSO(BEX bex) {
        db = dbHelper.getReadableDatabase();
        ArrayList<String> sos = new ArrayList<String>();
        String sql = "SELECT DISTINCT " + ConstSQLite.KEY_HEADER_NO_ORDER + " FROM " + ConstSQLite.TABLE_HEADER +
                " AS H JOIN " + ConstSQLite.TABLE_LINE + " AS L " +
                " ON H." + ConstSQLite.KEY_HEADER_ID + " = L." + ConstSQLite.KEY_LINE_HEADER_ID +
                " WHERE " + ConstSQLite.KEY_HEADER_ID_KEGIATAN + " IN (" +
                "SELECT " + ConstSQLite.KEG_ID + " FROM " + ConstSQLite.TABLE_KEGIATAN + " WHERE " +
                ConstSQLite.KEY_BEX_NO + " = " + bex.getEmpNo() + ") " +
                " AND " + ConstSQLite.KEY_LINE_ERROR + " = 1 " +
                " ORDER BY " + ConstSQLite.KEY_HEADER_NO_ORDER;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                sos.add(c.getString(c.getColumnIndex(ConstSQLite.KEY_HEADER_NO_ORDER)));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return sos;
    }

    public ArrayList<String> getItemError(String andro_so) {
        db = dbHelper.getReadableDatabase();
        ArrayList<String> sos = new ArrayList<String>();
        String sql = "SELECT DISTINCT " + ConstSQLite.KEY_LINE_ITEM_CODE + " FROM " + ConstSQLite.TABLE_LINE +
                " AS L JOIN " + ConstSQLite.TABLE_HEADER + " AS H " +
                " ON L." + ConstSQLite.KEY_HEADER_ID + " = H." + ConstSQLite.KEY_LINE_HEADER_ID +
                " WHERE " + ConstSQLite.KEY_HEADER_NO_ORDER + " = '" + andro_so + "'" +
                " AND " + ConstSQLite.KEY_LINE_ERROR + " = 1 " +
                " ORDER BY " + ConstSQLite.KEY_LINE_ITEM_CODE;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                sos.add(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_CODE)));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return sos;
    }

    void Delete(int line_id) {
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + ConstSQLite.KEY_LINE_ID + " = " + String.valueOf(line_id);
        db.execSQL(query);
        db.close();
    }

    public ArrayList<Item> Find(String teks) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT  T1." + ConstSQLite.KEY_LINE_ITEM_CODE + "," +
                ConstSQLite.KEY_LINE_ITEM_DESC + ", " +
                ConstSQLite.KEY_LINE_ITEM_UNIT + ", " +
                "T1." + ConstSQLite.KEY_LINE_ITEM_PRICE + "," +
                "T2.Last_Date AS " + ConstSQLite.INPUT_DATE +
                " FROM " + ConstSQLite.TABLE_LINE + " AS T1 " +
                "INNER JOIN (SELECT " +
                ConstSQLite.KEY_LINE_ITEM_CODE + ", " +
                "MAX(" + ConstSQLite.INPUT_DATE + ") AS Last_Date " +
                " FROM " + ConstSQLite.TABLE_LINE +
                " WHERE " + ConstSQLite.KEY_LINE_ITEM_PRICE + " <> 0" +
                " GROUP BY " + ConstSQLite.KEY_LINE_ITEM_CODE + ") AS T2 " +
                " ON T1." + ConstSQLite.KEY_LINE_ITEM_CODE + " = T2." + ConstSQLite.KEY_LINE_ITEM_CODE +
                " AND T1." + ConstSQLite.INPUT_DATE + " = T2.Last_Date " +
                "WHERE " +
                "T1." + ConstSQLite.KEY_LINE_ITEM_CODE + " LIKE '%" + teks + "%' OR " +
                "T1." + ConstSQLite.KEY_LINE_ITEM_DESC + " LIKE '%" + teks + "%' LIMIT 10";

        ArrayList<Item> list_item = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Item item = new Item();
                item.setCode(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_CODE)));
                item.setDesc(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_DESC)));
                item.setPrice(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_PRICE)));
                item.setUnit(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_UNIT)));
                item.setUnit(c.getString(c.getColumnIndex(ConstSQLite.KEY_LINE_ITEM_UNIT)));
                item.setInputDate(c.getString(c.getColumnIndex(ConstSQLite.INPUT_DATE)));
                list_item.add(item);
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_item;
    }
}
