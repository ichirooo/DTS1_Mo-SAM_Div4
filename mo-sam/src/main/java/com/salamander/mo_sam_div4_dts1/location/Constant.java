package com.salamander.mo_sam_div4_dts1.location;

public class Constant {

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static String PACKAGE_NAME = "com.salamander.mo_sam_div4_dts1.location";
    public static String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static String LOCATION_RECEIVER = PACKAGE_NAME + ".LOCATION_RECEIVER";
    public static String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";

    public static void setPackageName(String packageName) {
        PACKAGE_NAME = packageName;
        RECEIVER = PACKAGE_NAME + ".RECEIVER";
        LOCATION_RECEIVER = PACKAGE_NAME + ".LOCATION_RECEIVER";
        RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
        RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
        LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";
    }
}
