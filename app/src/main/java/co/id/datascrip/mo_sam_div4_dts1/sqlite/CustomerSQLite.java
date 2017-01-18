package co.id.datascrip.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.object.Customer;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;

public class CustomerSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public CustomerSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    public boolean Insert(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEY_CUSTOMER_KEG_ID, kegiatan.getID());
            values.put(ConstSQLite.KEY_CUSTOMER_CODE, kegiatan.getSalesHeader().getCustomer().getCode());
            values.put(ConstSQLite.KEY_CUSTOMER_NAME, kegiatan.getSalesHeader().getCustomer().getName());
            values.put(ConstSQLite.KEY_CUSTOMER_ADDRESS_1, kegiatan.getSalesHeader().getCustomer().getAddress1());
            values.put(ConstSQLite.KEY_CUSTOMER_ADDRESS_2, kegiatan.getSalesHeader().getCustomer().getAddress2());
            values.put(ConstSQLite.KEY_CUSTOMER_CONTACT, kegiatan.getSalesHeader().getCustomer().getContact());
            values.put(ConstSQLite.KEY_CUSTOMER_CITY, kegiatan.getSalesHeader().getCustomer().getCity());
            values.put(ConstSQLite.KEY_CUSTOMER_NPWP, kegiatan.getSalesHeader().getCustomer().getNPWP());
            values.put(ConstSQLite.KEY_CUSTOMER_PHONE, kegiatan.getSalesHeader().getCustomer().getPhone());
            values.put(ConstSQLite.KEY_CUSTOMER_LATITUDE, kegiatan.getSalesHeader().getCustomer().getLatitude());
            values.put(ConstSQLite.KEY_CUSTOMER_LONGITUDE, kegiatan.getSalesHeader().getCustomer().getLongitude());

            db.insert(ConstSQLite.TABLE_CUSTOMER, null, values);
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public boolean Update(Kegiatan kegiatan) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEY_CUSTOMER_KEG_ID, kegiatan.getID());
            values.put(ConstSQLite.KEY_CUSTOMER_CODE, kegiatan.getSalesHeader().getCustomer().getCode());
            values.put(ConstSQLite.KEY_CUSTOMER_NAME, kegiatan.getSalesHeader().getCustomer().getName());
            values.put(ConstSQLite.KEY_CUSTOMER_ADDRESS_1, kegiatan.getSalesHeader().getCustomer().getAddress1());
            values.put(ConstSQLite.KEY_CUSTOMER_ADDRESS_2, kegiatan.getSalesHeader().getCustomer().getAddress2());
            values.put(ConstSQLite.KEY_CUSTOMER_CITY, kegiatan.getSalesHeader().getCustomer().getCity());
            values.put(ConstSQLite.KEY_CUSTOMER_CONTACT, kegiatan.getSalesHeader().getCustomer().getContact());
            values.put(ConstSQLite.KEY_CUSTOMER_NPWP, kegiatan.getSalesHeader().getCustomer().getNPWP());
            values.put(ConstSQLite.KEY_CUSTOMER_PHONE, kegiatan.getSalesHeader().getCustomer().getPhone());
            values.put(ConstSQLite.KEY_CUSTOMER_LATITUDE, kegiatan.getSalesHeader().getCustomer().getLatitude());
            values.put(ConstSQLite.KEY_CUSTOMER_LONGITUDE, kegiatan.getSalesHeader().getCustomer().getLongitude());

            db.update(ConstSQLite.TABLE_CUSTOMER, values, ConstSQLite.KEY_CUSTOMER_KEG_ID + " = ?",
                    new String[]{String.valueOf(kegiatan.getID())});
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public boolean Post(Kegiatan kegiatan) {
        if (isExist(kegiatan))
            return Update(kegiatan);
        else
            return Insert(kegiatan);
    }

    public boolean isExist(Kegiatan kegiatan) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_CUSTOMER + " WHERE " +
                ConstSQLite.KEY_CUSTOMER_KEG_ID + " = " + String.valueOf(kegiatan.getID());
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        db.close();
        c.close();
        return exist;
    }

    public Customer get(int id_kegiatan) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_CUSTOMER + " WHERE " +
                ConstSQLite.KEY_CUSTOMER_KEG_ID + " = " + id_kegiatan;

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            Customer customer = new Customer();
            customer.setCode(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_CODE)));
            customer.setName(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_NAME)));
            customer.setAddress1(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_ADDRESS_1)));
            customer.setAddress2(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_ADDRESS_2)));
            customer.setCity(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_CITY)));
            customer.setContact(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_CONTACT)));
            customer.setNPWP(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_NPWP)));
            customer.setPhone(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_PHONE)));
            customer.setLatitude(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_LATITUDE)));
            customer.setLongitude(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_LONGITUDE)));
            db.close();
            c.close();
            return customer;
        } else {
            db.close();
            c.close();
            return null;
        }
    }

    public ArrayList<Customer> Find(String teks) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT DISTINCT " +
                ConstSQLite.KEY_CUSTOMER_CODE + ", " +
                ConstSQLite.KEY_CUSTOMER_NAME + ", " +
                ConstSQLite.KEY_CUSTOMER_ADDRESS_1 + ", " +
                ConstSQLite.KEY_CUSTOMER_ADDRESS_2 + ", " +
                ConstSQLite.KEY_CUSTOMER_CITY + ", " +
                ConstSQLite.KEY_CUSTOMER_CONTACT + ", " +
                ConstSQLite.KEY_CUSTOMER_NPWP + ", " +
                ConstSQLite.KEY_CUSTOMER_PHONE + ", " +
                ConstSQLite.KEY_CUSTOMER_LATITUDE + ", " +
                ConstSQLite.KEY_CUSTOMER_LONGITUDE +
                " FROM " + ConstSQLite.TABLE_CUSTOMER + " WHERE " +
                ConstSQLite.KEY_CUSTOMER_NAME + " LIKE '%" + teks + "%' OR " +
                ConstSQLite.KEY_CUSTOMER_CODE + " LIKE '%" + teks + "%' LIMIT 10";

        ArrayList<Customer> list_customer = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setCode(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_CODE)));
                customer.setName(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_NAME)));
                customer.setAddress1(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_ADDRESS_1)));
                customer.setAddress2(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_ADDRESS_2)));
                customer.setCity(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_CITY)));
                customer.setContact(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_CONTACT)));
                customer.setNPWP(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_NPWP)));
                customer.setPhone(c.getString(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_PHONE)));
                customer.setLatitude(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_LATITUDE)));
                customer.setLongitude(c.getDouble(c.getColumnIndex(ConstSQLite.KEY_CUSTOMER_LONGITUDE)));
                list_customer.add(customer);
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_customer;
    }
}
