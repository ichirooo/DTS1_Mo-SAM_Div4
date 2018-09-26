package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salamander.mo_sam_div4_dts1.object.Customer;

import java.util.ArrayList;

public class CustomerSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public CustomerSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    public Customer Insert(Customer customer) {
        try {
            db = dbHelper.getWritableDatabase();
            db.insert(ConstSQLite.TABLE_CUSTOMER, null, customerToContentValues(customer));
            db.close();

            return get(customer.getIDKegiatan());
        } catch (Exception e) {
            // TODO: handle exception
            return customer;
        }
    }

    public Customer Update(Customer customer) {
        try {
            db = dbHelper.getWritableDatabase();
            db.update(ConstSQLite.TABLE_CUSTOMER, customerToContentValues(customer), Customer.CUSTOMER_ID_KEGIATAN + " = ? AND " + Customer.CUSTOMER_CODE + " = ?",
                    new String[]{String.valueOf(customer.getIDKegiatan()), customer.getCode()});
            db.close();

            return get(customer.getIDKegiatan());
        } catch (Exception e) {
            // TODO: handle exception
            return customer;
        }
    }

    public Customer Post(Customer customer) {
        if (isExist(customer))
            return Update(customer);
        else
            return Insert(customer);
    }

    public boolean isExist(Customer customer) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_CUSTOMER + " WHERE " +
                Customer.CUSTOMER_ID_KEGIATAN + " = " + String.valueOf(customer.getIDKegiatan()) +
                " AND " + Customer.CUSTOMER_CODE + " = '" + String.valueOf(customer.getCode()) + "'";
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        db.close();
        c.close();
        return exist;
    }

    public Customer get(String customerCode) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_CUSTOMER + " WHERE " +
                Customer.CUSTOMER_CODE + " = '" + customerCode + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            Customer customer = cursorToCustomer(cursor);
            db.close();
            cursor.close();
            return customer;
        } else {
            db.close();
            cursor.close();
            return null;
        }
    }

    public Customer get(int id_kegiatan) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_CUSTOMER + " WHERE " +
                Customer.CUSTOMER_ID_KEGIATAN + " = " + id_kegiatan;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            Customer customer = cursorToCustomer(cursor);
            db.close();
            cursor.close();
            return customer;
        } else {
            db.close();
            cursor.close();
            return null;
        }
    }

    public ArrayList<Customer> find(String teks) {
        return find(teks, false);
    }

    public ArrayList<Customer> find(String teks, boolean hideCustomerLead) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT DISTINCT " +
                Customer.CUSTOMER_CODE + ", " +
                Customer.CUSTOMER_NAME + ", " +
                Customer.CUSTOMER_ADDRESS_1 + ", " +
                Customer.CUSTOMER_ADDRESS_2 + ", " +
                Customer.CUSTOMER_CITY + ", " +
                Customer.CUSTOMER_CONTACT_PERSON + ", " +
                Customer.CUSTOMER_NPWP + ", " +
                Customer.CUSTOMER_PHONE + ", " +
                Customer.CUSTOMER_LATITUDE + ", " +
                Customer.CUSTOMER_LONGITUDE +
                " FROM " + ConstSQLite.TABLE_CUSTOMER + " WHERE " +
                Customer.CUSTOMER_CODE + " LIKE '%" + teks + "%' OR " +
                Customer.CUSTOMER_NAME + " LIKE '%" + teks + "%' LIMIT 10";

        ArrayList<Customer> list_customer = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list_customer.add(cursorToCustomer(cursor));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return list_customer;
    }

    public void deleteByKegiatan(int kegId) {
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_CUSTOMER + " WHERE " +
                Customer.CUSTOMER_ID_KEGIATAN + " = " + String.valueOf(kegId);
        db.execSQL(query);
        db.close();
    }

    public ArrayList<Customer> getAllCustomerLeadBySalesperson() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT *  FROM " + ConstSQLite.TABLE_CUSTOMER + //" WHERE " +
                //Customer.CUSTOMER_BEX + " = " + String.valueOf(App.getUser(context).getEmpNo()) +
                //" AND " + Customer.CUSTOMER_CODE + " LIKE 'CUST%' " +
                " ORDER BY " + Customer.CUSTOMER_CODE;

        ArrayList<Customer> list_customer = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list_customer.add(cursorToCustomer(c));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_customer;
    }

    private ContentValues customerToContentValues(Customer customer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Customer.CUSTOMER_ID_KEGIATAN, customer.getIDKegiatan());
        contentValues.put(Customer.CUSTOMER_BEX, customer.getBEX());
        contentValues.put(Customer.CUSTOMER_CODE, customer.getCode());
        contentValues.put(Customer.CUSTOMER_NAME, customer.getName());
        contentValues.put(Customer.CUSTOMER_ALIAS, customer.getAlias());
        contentValues.put(Customer.CUSTOMER_ADDRESS_1, customer.getAddress1());
        contentValues.put(Customer.CUSTOMER_ADDRESS_2, customer.getAddress2());
        contentValues.put(Customer.CUSTOMER_CONTACT_PERSON, customer.getContactPerson());
        contentValues.put(Customer.CUSTOMER_CITY, customer.getCity());
        contentValues.put(Customer.CUSTOMER_NPWP, customer.getNPWP());
        contentValues.put(Customer.CUSTOMER_PHONE, customer.getPhone());
        contentValues.put(Customer.CUSTOMER_LATITUDE, customer.getLatitude());
        contentValues.put(Customer.CUSTOMER_LONGITUDE, customer.getLongitude());
        return contentValues;
    }

    private Customer cursorToCustomer(Cursor cursor) {
        Customer customer = new Customer();
        customer.setIDKegiatan(ConstSQLite.getInt(cursor, Customer.CUSTOMER_ID_KEGIATAN));
        customer.setBEX(ConstSQLite.getInt(cursor, Customer.CUSTOMER_BEX));
        customer.setCode(ConstSQLite.getString(cursor, Customer.CUSTOMER_CODE));
        customer.setName(ConstSQLite.getString(cursor, Customer.CUSTOMER_NAME));
        customer.setAlias(ConstSQLite.getString(cursor, Customer.CUSTOMER_ALIAS));
        customer.setAddress1(ConstSQLite.getString(cursor, Customer.CUSTOMER_ADDRESS_1));
        customer.setAddress2(ConstSQLite.getString(cursor, Customer.CUSTOMER_ADDRESS_2));
        customer.setContactPerson(ConstSQLite.getString(cursor, Customer.CUSTOMER_CONTACT_PERSON));
        customer.setCity(ConstSQLite.getString(cursor, Customer.CUSTOMER_CITY));
        customer.setNPWP(ConstSQLite.getString(cursor, Customer.CUSTOMER_NPWP));
        customer.setPhone(ConstSQLite.getString(cursor, Customer.CUSTOMER_PHONE));
        customer.setLatitude(ConstSQLite.getDouble(cursor, Customer.CUSTOMER_LATITUDE));
        customer.setLongitude(ConstSQLite.getDouble(cursor, Customer.CUSTOMER_LONGITUDE));
        return customer;
    }
}
