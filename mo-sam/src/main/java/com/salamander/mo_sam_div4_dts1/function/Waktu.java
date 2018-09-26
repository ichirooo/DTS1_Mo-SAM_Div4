package com.salamander.mo_sam_div4_dts1.function;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Waktu {

    public static int mYear, mMonth, mDate, mHour, mMin;
    public static int mYearStart, mMonthStart, mDateStart, mHourStart, mMinStart;
    public static int mYearEnd, mMonthEnd, mDateEnd, mHourEnd, mMinEnd;
    public static int mYearPromised, mMonthPromised, mDatePromised;
    public static GregorianCalendar date, dateStart, dateEnd, datePromised;

    public Waktu() {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDate = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMin = c.get(Calendar.MINUTE);
        date = new GregorianCalendar(mYear, mMonth, mDate, mHour, mMin, 0);
        date.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        mYearStart = c.get(Calendar.YEAR);
        mMonthStart = c.get(Calendar.MONTH);
        mDateStart = c.get(Calendar.DAY_OF_MONTH);
        mHourStart = c.get(Calendar.HOUR_OF_DAY);
        mMinStart = c.get(Calendar.MINUTE);
        dateStart = new GregorianCalendar(mYearStart, mMonthStart, mDateStart, mHourStart, mMinStart, 0);
        dateStart.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        mYearEnd = c.get(Calendar.YEAR);
        mMonthEnd = c.get(Calendar.MONTH);
        mDateEnd = c.get(Calendar.DAY_OF_MONTH);
        mHourEnd = c.get(Calendar.HOUR_OF_DAY);
        mMinEnd = c.get(Calendar.MINUTE);
        dateEnd = new GregorianCalendar(mYearEnd, mMonthEnd, mDateEnd, mHourEnd, mMinEnd, 0);
        dateEnd.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        c.add(Calendar.DAY_OF_MONTH, 1);
        mYearPromised = c.get(Calendar.YEAR);
        mMonthPromised = c.get(Calendar.MONTH);
        mDatePromised = c.get(Calendar.DAY_OF_MONTH);
        datePromised = new GregorianCalendar(mYearPromised, mMonthPromised, mDatePromised, 0, 0, 0);
        datePromised.setTimeZone(TimeZone.getTimeZone("GMT+7"));
    }

    public String getCurrent() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String strDate = sdf.format(new Date());
        return strDate;
        //"yyyy-MM-dd HH:mm:ss"
    }

    public String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String strDate = sdf.format(date);
        return strDate;
    }

    public String getStartDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String strDate = sdf.format(dateStart);
        return strDate;
    }

    public String getEndDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String strDate = sdf.format(dateEnd);
        return strDate;
    }

    public String getPromisedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String strDate = sdf.format(datePromised);
        return strDate;
    }

}
