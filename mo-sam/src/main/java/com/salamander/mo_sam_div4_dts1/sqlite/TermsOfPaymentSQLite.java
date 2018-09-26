package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class TermsOfPaymentSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public TermsOfPaymentSQLite(Context context) {
        dbHelper = new DatabaseHelper(context);
        this.context = context;
    }

    public void clear() {
        db = dbHelper.getReadableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_TERMS_OF_PAYMENT;
        db.execSQL(query);
        db.close();
    }

    private boolean Insert(String termOfPayment) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ConstSQLite.TERMS_CODE, termOfPayment);
            long result = db.insertOrThrow(ConstSQLite.TABLE_TERMS_OF_PAYMENT, null, contentValues);
            db.close();

            return true;
        } catch (Exception e) {
            //FileUtil.writeExceptionLog(context, TermsOfPaymentSQLite.class.getSimpleName() + " => insert => ", e);
            return false;
        }
    }

    public boolean Post(String termOfPayment) {
        return Insert(termOfPayment);
    }

    public ArrayList<String> getAll() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_TERMS_OF_PAYMENT;

        ArrayList<String> list_terms = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list_terms.add(ConstSQLite.getString(c, ConstSQLite.TERMS_CODE));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_terms;
    }

}
