package co.id.datascrip.mo_sam_div4_dts1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import co.id.datascrip.mo_sam_div4_dts1.activity.Login_Activity;
import co.id.datascrip.mo_sam_div4_dts1.activity.MainActivity;
import co.id.datascrip.mo_sam_div4_dts1.object.BEX;

public class SessionManager {

    private static final String PREF_NAME = "MoSAM_D4_DTS1";
    private static final String IS_LOGIN = "isLoggedIn";
    private static final int PRIVATE_MODE = 0;
    private SharedPreferences spf;
    private Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        this.spf = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = spf.edit();
        editor.commit();
    }

    public void createLoginSession(BEX bex) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString("emp_no", bex.getEmpNo());
        editor.putString("initial", bex.getInitial());
        editor.putString("fullname", bex.getFullname());
        editor.putString("photo", bex.getPhoto());
        editor.putString("defaultTerms", Global.defaultTerms);
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
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public BEX getUserLogin() {
        BEX bex = new BEX();
        bex.setEmpNo(spf.getString("emp_no", null));
        bex.setInitial(spf.getString("initial", null));
        bex.setFullname(spf.getString("fullname", null));
        bex.setPhoto(spf.getString("photo", null));
        Global.defaultTerms = spf.getString("defaultTerms", null);

        return bex;
    }

    public void setLastLocation(Date date, float latitude, float longitude) {
        editor.putLong("last_time", date.getTime());
        editor.putFloat("last_lat", latitude);
        editor.putFloat("last_long", longitude);
        editor.apply();
    }

    public LatLng getLastLocation() {
        return new LatLng(spf.getFloat("last_lat", 0), spf.getFloat("last_long", 0));
    }

    public long getLastLocationTime() {
        return spf.getLong("last_time", 0);
    }

    public boolean isLogin() {
        return spf.getBoolean(IS_LOGIN, false);
    }

}
