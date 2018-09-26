package com.salamander.mo_sam_div4_dts1;

import android.os.Environment;

public class Const {

    public static final String CERTIFICATE_PATH = Environment.getExternalStorageDirectory() + "/Mo-SAM/certificate/datascrip_co_id.crt";

    public static final int POSITION_UPDATE = 5 * 60 * 1000;
    public static final int FEEDBACK_INTERVAL = 5 * 1000;
    public static final int time_out = 30 * 1000;
    public static final int DEFAULT_HEADER_ID = 1000000000;

    public static final int KEGIATAN_TODAY = 10;
    public static final int KEGIATAN_THIS_MONTH = 11;

    public static final int KEGIATAN_BY_DATE = 0;
    public static final int KEGIATAN_BY_MONTH = 1;

    public static final int CHECK_IN = 1;
    public static final int CHECK_OUT = 0;

    public static final int ACTION_CANCEL = 0;
    public static final int ACTION_KETERANGAN = 1;
    public static final int ACTION_RESULT = 2;

    public static final String ACTION_CHECK_IN = "in";
    public static final String ACTION_CHECK_OUT = "out";

    public static final String CAPTION_LATITUDE = "Latitude";
    public static final String CAPTION_LONGITUDE = "Longitude";

    public static final String BROADCAST_ACTION = "com.salamander.mo_sam_div4_dts1.service.TestReceiver";
    public static final String BROADCAST_BEX_KEY = "com.salamander.mo_sam_div4_dts1.service.User";
}
