package com.salamander.mo_sam_div4_dts1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;

import com.salamander.mo_sam_div4_dts1.activity.Login_Activity;
import com.salamander.mo_sam_div4_dts1.activity.Main_Activity;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.salamander_location.LocationInfo;
import com.salamander.salamander_location.SalamanderLocation;

import java.io.File;
import java.util.Date;

public class SessionManager {

    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MoSAM_D4_DTS1";
    public static final String PREF_CERT_FILE = PREF_NAME + "_certificate";
    private static final String IS_LOGIN = "isLoggedIn";
    private static final String LAST_POSITION_LATITUDE = PREF_NAME + "_latitude";
    private static final String LAST_POSITION_LONGITUDE = PREF_NAME + "_longitude";
    private static final String LAST_POSITION_TIMESTAMP = PREF_NAME + "_timestamp";
    private static final String KEGIATAN_CHECKED_IN = PREF_NAME + "_checkedInKegiatan";
    private static final String KEGIATAN_LAST_CHECKED_IN = PREF_NAME + "_lastCheckedIn";
    private SharedPreferences spf;
    private Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        this.spf = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = spf.edit();
        editor.apply();
    }

    public void createLoginSession(User user) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt("emp_no", user.getEmpNo());
        editor.putString("initial", user.getInitial());
        editor.putString("fullname", user.getFullname());
        editor.putString("photo", user.getPhoto());
        editor.putString("defaultTerms", App.defaultTerms);
        editor.commit();
    }

    public void LogoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, Login_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void checkLogin() {
        if (this.isLogin()) {
            Intent intent = new Intent(context, Main_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public User getUserLogin() {
        User user = new User();
        user.setEmpNo(spf.getInt("emp_no", 0));
        user.setInitial(spf.getString("initial", null));
        user.setFullname(spf.getString("fullname", null));
        user.setPhoto(spf.getString("photo", null));
        App.defaultTerms = spf.getString("defaultTerms", null);

        return user;
    }

    /*
        public void setLastLocation(Date date, float latitude, float longitude) {
            editor.putLong("last_time", date.getTime());
            editor.putFloat("last_lat", latitude);
            editor.putFloat("last_long", longitude);
            editor.apply();
        }

        public Location getLastLocation() {
            Location location = new Location("LAST_LOCATION");
            location.setLatitude(getDouble("last_lat", 0));
            location.setLongitude(getDouble("last_long", 0));
            location.setTime(spf.getLong("last_time", 0));
            return location;
        }

        public void setLastLocation(long timestamp, double latitude, double longitude) {
            editor = spf.edit();
            editor.putLong("last_time", timestamp);
            putDouble("last_lat", latitude);
            putDouble("last_long", longitude);
            editor.apply();
        }
        */
    public Location getLastLocation() {

        Location location = new Location("LAST_LOCATION");
        location.setLatitude(getDouble(LAST_POSITION_LATITUDE, 0));
        location.setLongitude(getDouble(LAST_POSITION_LONGITUDE, 0));
        location.setTime(spf.getLong(LAST_POSITION_TIMESTAMP, 0));
        if (location.getLatitude() == 0 || location.getLongitude() == 0) {
            LocationInfo locationInfo = SalamanderLocation.getLastLocation(context.getApplicationContext());
            location.setLatitude(locationInfo.getLatitude());
            location.setLongitude(locationInfo.getLongitude());
            location.setTime(locationInfo.getTime());
        }
        return location;
    }

    public void setLastLocation(long timestamp, double latitude, double longitude) {
        editor = spf.edit();
        editor.putLong(LAST_POSITION_TIMESTAMP, timestamp);
        putDouble(LAST_POSITION_LATITUDE, latitude);
        putDouble(LAST_POSITION_LONGITUDE, longitude);
        editor.apply();
    }

    public long getLastLocationTime() {
        return spf.getLong("last_time", 0);
    }

    public boolean isLogin() {
        return spf.getBoolean(IS_LOGIN, false);
    }

    public int getCheckedInKegiatan() {
        return spf.getInt(KEGIATAN_CHECKED_IN, 0);
    }

    public void setCheckedInKegiatan(int id_kegiatan) {
        editor = spf.edit();
        editor.putInt(KEGIATAN_CHECKED_IN, id_kegiatan);
        editor.putLong(KEGIATAN_LAST_CHECKED_IN, new Date().getTime());
        editor.apply();
    }

    public long getLastCheckIn() {
        return spf.getLong(KEGIATAN_LAST_CHECKED_IN, 0);
    }

    public void setLastCheckIn(long milis) {
        editor = spf.edit();
        editor.putLong(KEGIATAN_LAST_CHECKED_IN, milis);
        editor.apply();
    }

    public File getCertFile() {
        return new File(spf.getString(PREF_CERT_FILE, ""));
    }

    public void setCertFile(File file) {
        editor = spf.edit();
        editor.putString(PREF_CERT_FILE, file.getAbsolutePath());
        editor.apply();
    }

    private void putDouble(final String key, final double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
    }

    private double getDouble(final String key, final double defaultValue) {
        return Double.longBitsToDouble(spf.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
