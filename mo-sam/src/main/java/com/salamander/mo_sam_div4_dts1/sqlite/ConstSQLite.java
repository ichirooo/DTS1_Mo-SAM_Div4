package com.salamander.mo_sam_div4_dts1.sqlite;

import android.database.Cursor;

import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.object.Feedback;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.Log_Feed;
import com.salamander.mo_sam_div4_dts1.object.Photo;
import com.salamander.mo_sam_div4_dts1.object.SalesHeader;
import com.salamander.mo_sam_div4_dts1.object.User;

public class ConstSQLite {

    public static final int DATABASE_VERSION = 1;
    public static final int LINE_NO_INCREMENT = 100;
    public static final int TEMP_ID_SALES_HEADER = 10000000;

    public static final String DATABASE_NAME = "Mo_SAM_Div4_DTS1.sqlite";
    public static final String TABLE_HEADER = "tbl_sales_header";
    public static final String TABLE_LINE = "tbl_sales_line";
    public static final String TABLE_CUSTOMER = "tbl_customer";
    public static final String TABLE_KEGIATAN = "tbl_kegiatan";
    public static final String TABLE_PHOTO = "tbl_photo";
    public static final String TABLE_LOG = "tbl_log";

    /*
    public static final String KEY_BEX_NO = "Bex_No";
    public static final String KEY_BEX_INITIAL = "Bex_Initial";
    public static final String INPUT_DATE = "Input_Date";

    //Table Kegiatan
    public static final String KEGIATAN_ID = "ID";
    public static final String KEGIATAN_TIPE = "Tipe";
    public static final String KEGIATAN_JENIS = "Jenis";
    public static final String KEGIATAN_SUBJECT = "Subject";
    public static final String KEGIATAN_KETERANGAN = "Keterangan";
    public static final String KEGIATAN_RESULT = "Result";
    public static final String KEGIATAN_CANCEL = "Cancel";
    public static final String KEGIATAN_CANCEL_REASON = "Cancel_Reason";
    public static final String KEGIATAN_CHECK_IN = "Check_In";
    public static final String KEGIATAN_START_DATE = "Start_Date";
    public static final String KEGIATAN_END_DATE = "End_Date";

    // Table Header
    public static final String KEY_HEADER_ID = "HeaderID";
    public static final String KEY_HEADER_ID_KEGIATAN = "ID_Kegiatan";
    public static final String KEY_HEADER_NO_ORDER = "No_Order";
    public static final String KEY_HEADER_CURRENCY = "Currency";
    public static final String KEY_HEADER_NOTES = "Notes";
    public static final String KEY_HEADER_SYARAT = "Syarat";
    public static final String KEY_HEADER_TEMPO = "Tempo";
    public static final String KEY_HEADER_STATUS = "Status";
    public static final String KEY_HEADER_PROMISED_DATE = "Promised_Date";

    //Total
    public static final String KEY_TOTAL_SUBTOTAL = "SubTotal";
    public static final String KEY_TOTAL_DISCOUNT = "Discount";
    public static final String KEY_TOTAL_DPP = "DPP";
    public static final String KEY_TOTAL_TAX = "TAX";
    public static final String KEY_TOTAL_GRANDTOTAL = "GrandTotal";

    //Table Line
    public static final String KEY_LINE_ID = "LineID";
    public static final String KEY_LINE_HEADER_ID = "HeaderID";
    public static final String KEY_LINE_ITEM_CODE = "Item_Code";
    public static final String KEY_LINE_ITEM_DESC = "Description";
    public static final String KEY_LINE_ITEM_PRICE = "Price";
    public static final String KEY_LINE_ITEM_DISC = "Discount";
    public static final String KEY_LINE_ITEM_DISC_PCT = "DiscountPct";
    public static final String KEY_LINE_ITEM_QTY = "Qty";
    public static final String KEY_LINE_ITEM_UNIT = "Unit";
    public static final String KEY_LINE_ITEM_SUBTOTAL = "Subtotal";
    public static final String KEY_LINE_ERROR = "Error";
    public static final String KEY_LINE_NOTES_FEEDBACK = "Feedback";

    //Table Customer
    public static final String KEY_CUSTOMER_KEG_ID = "ID_Kegiatan";
    public static final String KEY_CUSTOMER_CODE = "Code";
    public static final String KEY_CUSTOMER_NAME = "Name";
    public static final String KEY_CUSTOMER_ADDRESS_1 = "Address1";
    public static final String KEY_CUSTOMER_ADDRESS_2 = "Address2";
    public static final String KEY_CUSTOMER_CITY = "City";
    public static final String KEY_CUSTOMER_CONTACT = "Kontak";
    public static final String KEY_CUSTOMER_NPWP = "NPWP";
    public static final String KEY_CUSTOMER_PHONE = "Phone";
    public static final String KEY_CUSTOMER_LATITUDE = "Latitude";
    public static final String KEY_CUSTOMER_LONGITUDE = "Longitude";
    */

    //Table Photo
    public static final String KEY_PHOTO_ID = "ID";
    public static final String KEY_PHOTO_ID_KEGIATAN = "ID_Kegiatan";
    public static final String KEY_PHOTO_PATH = "Path";
    public static final String KEY_PHOTO_INFO = "Info";
    public static final String KEY_PHOTO_POSTED = "Posted";

    //Table Log
    public static final String KEY_LOG_ID = "ID";
    public static final String KEY_LOG_LINE_ID = "LineID";
    public static final String KEY_LOG_DATETIME = "Date_Time";
    public static final String KEY_LOG_EMPLOYEE_NO = "Emp_No";
    public static final String KEY_LOG_NOTE = "Note";

    //Table Holiday
    public static final String TABLE_HOLIDAY = "tblHoliday";
    public static final String HOLIDAY_TANGGAL = "Tanggal";
    public static final String HOLIDAY_DESCRIPTION = "Description";

    //Table Terms Of Payment
    public static final String TABLE_TERMS_OF_PAYMENT = "tblTermsOfPayment";
    public static final String TERMS_CODE = "Code";
    // Create table syntax
    public static final String CREATE_TABLE_HEADER = "CREATE TABLE " + TABLE_HEADER + "(" +
            SalesHeader.SALES_HEADER_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            SalesHeader.SALES_HEADER_ID_SERVER + " INTEGER, " +
            SalesHeader.SALES_HEADER_ID_KEGIATAN + " INTEGER, " +
            SalesHeader.SALES_HEADER_ORDER_NO + " TEXT, " +
            SalesHeader.SALES_HEADER_SYARAT_PEMBAYARAN + " TEXT, " +
            SalesHeader.SALES_HEADER_TEMPO + " TEXT, " +
            SalesHeader.SALES_HEADER_CURRENCY + " TEXT, " +
            SalesHeader.SALES_HEADER_NOTES + " TEXT, " +
            SalesHeader.SALES_HEADER_STATUS + " INTEGER, " +
            SalesHeader.SALES_HEADER_PROMISED_DELIVERY_DATE + " DATETIME, " +
            SalesHeader.SALES_HEADER_SUBTOTAL + " DOUBLE, " +
            SalesHeader.SALES_HEADER_POTONGAN + " DOUBLE, " +
            SalesHeader.SALES_HEADER_DPP + " DOUBLE, " +
            SalesHeader.SALES_HEADER_DPP_PPN + " DOUBLE, " +
            SalesHeader.SALES_HEADER_TOTAL + " DOUBLE, " +
            SalesHeader.SALES_HEADER_INPUT_DATE + " DATETIME )";
    public static final String CREATE_TABLE_LINE = "CREATE TABLE " + TABLE_LINE + "(" +
            Item.ITEM_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Item.ITEM_ID_SERVER + " INTEGER, " +
            Item.ITEM_ID_HEADER + " INTEGER, " +
            Item.ITEM_ORDER_NO + " TEXT, " +
            Item.ITEM_LINE_NO + " INTEGER, " +
            Item.ITEM_CODE + " TEXT, " +
            Item.ITEM_DESCRIPTION + " TEXT, " +
            Item.ITEM_UNIT + " TEXT, " +
            Item.ITEM_QUANTITY + " INTEGER, " +
            Item.ITEM_PRICE + " DOUBLE, " +
            Item.ITEM_PRICE_NAV + " DOUBLE, " +
            Item.ITEM_DISCOUNT_VALUE + " DOUBLE, " +
            Item.ITEM_DISCOUNT_PCT + " DOUBLE, " +
            Item.ITEM_SUBTOTAL + " DOUBLE, " +
            Item.ITEM_IS_ERROR + " INTEGER, " +
            Item.ITEM_NOTES + " TEXT, " +
            Item.ITEM_INPUT_DATE + " DATETIME )";
    public static final String CREATE_TABLE_KEGIATAN = "CREATE TABLE " + TABLE_KEGIATAN + "(" +
            Kegiatan.KEGIATAN_ID_LOKAL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Kegiatan.KEGIATAN_ID_SERVER + " INTEGER, " +
            User.BEX_EMP_NO + " INTEGER, " +
            User.BEX_INITIAL + " TEXT, " +
            Kegiatan.KEGIATAN_TIPE + " TEXT, " +
            Kegiatan.KEGIATAN_JENIS + " TEXT, " +
            Kegiatan.KEGIATAN_SUBJECT + " TEXT, " +
            Kegiatan.KEGIATAN_KETERANGAN + " TEXT, " +
            Kegiatan.KEGIATAN_RESULT + " TEXT, " +
            Kegiatan.KEGIATAN_RESULT_DATE + " DATETIME, " +
            Kegiatan.KEGIATAN_CANCELED + " INTEGER, " +
            Kegiatan.KEGIATAN_CANCEL_REASON + " TEXT, " +
            Kegiatan.KEGIATAN_CANCEL_DATE + " DATETIME, " +
            Kegiatan.KEGIATAN_CHECKED_IN + " INTEGER, " +
            Kegiatan.KEGIATAN_TGL_MULAI + " DATETIME, " +
            Kegiatan.KEGIATAN_TGL_SELESAI + " DATETIME, " +
            Kegiatan.KEGIATAN_INPUT_DATE + " DATETIME )";
    public static final String CREATE_TABLE_TBL_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "(" +
            Customer.CUSTOMER_ID_KEGIATAN + " INTEGER, " +
            Customer.CUSTOMER_BEX + " INTEGER, " +
            Customer.CUSTOMER_CODE + " TEXT, " +
            Customer.CUSTOMER_NAME + " TEXT, " +
            Customer.CUSTOMER_ALIAS + " TEXT, " +
            Customer.CUSTOMER_CITY + " TEXT, " +
            Customer.CUSTOMER_ADDRESS_1 + " TEXT, " +
            Customer.CUSTOMER_ADDRESS_2 + " TEXT, " +
            Customer.CUSTOMER_CONTACT_PERSON + " TEXT, " +
            Customer.CUSTOMER_PHONE + " TEXT, " +
            Customer.CUSTOMER_EMAIL + " TEXT, " +
            Customer.CUSTOMER_NPWP + " TEXT, " +
            Customer.CUSTOMER_LATITUDE + " DOUBLE, " +
            Customer.CUSTOMER_LONGITUDE + " DOUBLE )";
    public static final String CREATE_TABLE_PHOTO = "CREATE TABLE " + TABLE_PHOTO + "(" +
            Photo.PHOTO_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Photo.PHOTO_ID_SERVER + " INTEGER, " +
            Photo.PHOTO_ID_KEGIATAN + " INTEGER, " +
            Photo.PHOTO_PATH + " TEXT," +
            Photo.PHOTO_NAME + " TEXT," +
            Photo.PHOTO_URL + " TEXT," +
            Photo.PHOTO_DESCRIPTION + " TEXT," +
            Photo.PHOTO_TANGGAL + " DATETIME )";
    public static final String CREATE_TABLE_LOG = "CREATE TABLE " + TABLE_LOG + "(" +
            Log_Feed.LOG_FEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Feedback.FEEDBACK_LINE_ID + " INTEGER, " +
            Feedback.FEEDBACK_BEX + " TEXT," +
            Log_Feed.LOG_FEED_TANGGAL + " DATETIME," +
            Feedback.FEEDBACK_NOTE + " TEXT )";
    public static final String CREATE_TABLE_HOLIDAY = "CREATE TABLE " + TABLE_HOLIDAY + "(" +
            HOLIDAY_TANGGAL + " DATETIME, " +
            HOLIDAY_DESCRIPTION + " TEXT )";
    public static final String CREATE_TABLE_TERMS_OF_PAYMENT = "CREATE TABLE " + TABLE_TERMS_OF_PAYMENT + "(" +
            TERMS_CODE + " TEXT )";

    public static int getInt(Cursor cursor, String key) {
        return cursor.getInt(cursor.getColumnIndex(key));
    }

    public static String getString(Cursor cursor, String key) {
        return cursor.getString(cursor.getColumnIndex(key));
    }

    public static double getDouble(Cursor cursor, String key) {
        return cursor.getDouble(cursor.getColumnIndex(key));
    }
}
