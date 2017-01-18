package co.id.datascrip.mo_sam_div4_dts1.sqlite;

public class ConstSQLite {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "e-SAM_Div4_DTS1_DB.sqlite";
    public static final String TABLE_HEADER = "tbl_sales_header";
    public static final String TABLE_LINE = "tbl_sales_line";
    public static final String TABLE_CUSTOMER = "tbl_customer";
    public static final String TABLE_KEGIATAN = "tbl_kegiatan";
    public static final String TABLE_PHOTO = "tbl_photo";
    public static final String TABLE_LOG = "tbl_log";

    public static final String KEY_BEX_NO = "Bex_No";
    public static final String KEY_BEX_INITIAL = "Bex_Initial";
    public static final String INPUT_DATE = "Input_Date";

    //Table Kegiatan
    public static final String KEG_ID = "ID";
    public static final String KEG_TIPE = "Tipe";
    public static final String KEG_JENIS = "Jenis";
    public static final String KEG_SUBJECT = "Subject";
    public static final String KEG_KETERANGAN = "Keterangan";
    public static final String KEG_RESULT = "Result";
    public static final String KEG_CANCEL = "Cancel";
    public static final String KEG_CANCEL_REASON = "Cancel_Reason";
    public static final String KEG_CHECK_IN = "Check_In";
    public static final String KEG_START_DATE = "Start_Date";
    public static final String KEG_END_DATE = "End_Date";

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

    // Create table syntax
    public static final String CREATE_TABLE_HEADER = "CREATE TABLE " + TABLE_HEADER + "(" +
            KEY_HEADER_ID + " INTEGER, " +
            KEY_HEADER_ID_KEGIATAN + " INTEGER, " +
            KEY_HEADER_NO_ORDER + " TEXT, " +
            KEY_HEADER_SYARAT + " TEXT, " +
            KEY_HEADER_TEMPO + " TEXT, " +
            KEY_HEADER_CURRENCY + " TEXT, " +
            KEY_HEADER_NOTES + " TEXT, " +
            KEY_HEADER_STATUS + " INTEGER, " +
            KEY_HEADER_PROMISED_DATE + " DATETIME, " +
            KEY_TOTAL_SUBTOTAL + " DOUBLE, " +
            KEY_TOTAL_DISCOUNT + " DOUBLE, " +
            KEY_TOTAL_DPP + " DOUBLE, " +
            KEY_TOTAL_TAX + " DOUBLE, " +
            KEY_TOTAL_GRANDTOTAL + " DOUBLE, " +
            INPUT_DATE + " DATETIME )";

    public static final String CREATE_TABLE_LINE = "CREATE TABLE " + TABLE_LINE + "(" +
            KEY_LINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            KEY_LINE_HEADER_ID + " INTEGER, " +
            KEY_LINE_ITEM_CODE + " TEXT, " +
            KEY_LINE_ITEM_DESC + " TEXT, " +
            KEY_LINE_ITEM_UNIT + " TEXT, " +
            KEY_LINE_ITEM_QTY + " INTEGER, " +
            KEY_LINE_ITEM_PRICE + " DOUBLE, " +
            KEY_LINE_ITEM_DISC_PCT + " DOUBLE, " +
            KEY_LINE_ITEM_DISC + " DOUBLE, " +
            KEY_LINE_ITEM_SUBTOTAL + " DOUBLE, " +
            KEY_LINE_ERROR + " INTEGER, " +
            KEY_LINE_NOTES_FEEDBACK + " TEXT, " +
            INPUT_DATE + " DATETIME )";

    public static final String CREATE_TABLE_KEGIATAN = "CREATE TABLE " + TABLE_KEGIATAN + "(" +
            KEG_ID + " INTEGER, " +
            KEY_BEX_NO + " TEXT, " +
            KEY_BEX_INITIAL + " TEXT, " +
            KEG_TIPE + " TEXT, " +
            KEG_JENIS + " TEXT, " +
            KEG_SUBJECT + " TEXT, " +
            KEG_KETERANGAN + " TEXT, " +
            KEG_RESULT + " TEXT, " +
            KEG_CANCEL + " INTEGER, " +
            KEG_CANCEL_REASON + " TEXT, " +
            KEG_CHECK_IN + " INTEGER, " +
            KEG_START_DATE + " DATETIME, " +
            KEG_END_DATE + " DATETIME, " +
            INPUT_DATE + " DATETIME )";

    public static final String CREATE_TABLE_TBL_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "(" +
            KEY_CUSTOMER_KEG_ID + " INTEGER, " +
            KEY_CUSTOMER_CODE + " TEXT, " +
            KEY_CUSTOMER_NAME + " TEXT, " +
            KEY_CUSTOMER_CITY + " TEXT, " +
            KEY_CUSTOMER_ADDRESS_1 + " TEXT, " +
            KEY_CUSTOMER_ADDRESS_2 + " TEXT, " +
            KEY_CUSTOMER_CONTACT + " TEXT, " +
            KEY_CUSTOMER_NPWP + " TEXT, " +
            KEY_CUSTOMER_PHONE + " TEXT, " +
            KEY_CUSTOMER_LATITUDE + " DOUBLE, " +
            KEY_CUSTOMER_LONGITUDE + " DOUBLE )";

    public static final String CREATE_TABLE_PHOTO = "CREATE TABLE " + TABLE_PHOTO + "(" +
            KEY_PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            KEY_PHOTO_ID_KEGIATAN + " INTEGER, " +
            KEY_PHOTO_PATH + " TEXT," +
            KEY_PHOTO_INFO + " TEXT," +
            KEY_PHOTO_POSTED + " INTEGER )";

    public static final String CREATE_TABLE_LOG = "CREATE TABLE " + TABLE_LOG + "(" +
            KEY_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            KEY_LOG_LINE_ID + " INTEGER, " +
            KEY_LOG_EMPLOYEE_NO + " TEXT," +
            KEY_LOG_DATETIME + " DATETIME," +
            KEY_LOG_NOTE + " TEXT )";

}
