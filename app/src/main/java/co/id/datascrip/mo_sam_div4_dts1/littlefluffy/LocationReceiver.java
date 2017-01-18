package co.id.datascrip.mo_sam_div4_dts1.littlefluffy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.SessionManager;
import co.id.datascrip.mo_sam_div4_dts1.object.BEX;
import co.id.datascrip.mo_sam_div4_dts1.process.interfaces.IC_Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationReceiver extends BroadcastReceiver {

    private static boolean isRegistered;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocBroadcastReceiver", "onReceive: received location update");

        final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);

        SessionManager sessionManager = new SessionManager(context);
        KegiatanSQLite kegiatanSQLite = new KegiatanSQLite(context);
        BEX bex = sessionManager.getUserLogin();
        locationInfo.refresh(context);
        int id_kegiatan = kegiatanSQLite.getCurrentCheckIn();
        if (!((id_kegiatan == 0) || bex.getEmpNo() == null))
            Send_Position(context, makeJSON(String.valueOf(id_kegiatan), bex.getInitial(), bex.getEmpNo(), String.valueOf(locationInfo.lastLat), String.valueOf(locationInfo.lastLong)));
        sessionManager.setLastLocation(new Date(), locationInfo.lastLat, locationInfo.lastLong);
    }

    public void Send_Position(Context context, String json) {
        IC_Kegiatan IC = Global.CreateRetrofit(context).create(IC_Kegiatan.class);
        IC.SendPosition(json).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function().writeToText("LocationReceive->SendPosition", throwable.getMessage());
            }
        });
    }

    private String makeJSON(String id_kegiatan, String bex_initial, String bex_no, String latitude, String longitude) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id_kegiatan);
            json.put("lat", latitude);
            json.put("lang", longitude);
            json.put("bex", bex_initial);
            json.put("bex_no", bex_no);
        } catch (JSONException e) {
            new Function().writeToText("position_json", e.toString());
        }
        return json.toString();
    }

    public Intent register(Context context, IntentFilter intentFilter) {
        if (isRegistered) {
            unregister(context);
        }
        isRegistered = true;
        return context.registerReceiver(this, intentFilter);
    }

    public boolean unregister(Context context) {
        if (isRegistered) {
            context.unregisterReceiver(this);
            isRegistered = false;
            return true;
        }
        return false;
    }

    private boolean sendEnable(Context context, LocationInfo locationInfo) {
        SessionManager sm = new SessionManager(context);

        double lastLat = sm.getLastLocation().latitude;
        double lastLong = sm.getLastLocation().longitude;
        Date dateNow = new Date();
        Date dateLast = new Date(sm.getLastLocationTime());

        int diffMinute = (int) TimeUnit.MINUTES.toMinutes(dateNow.getTime() - dateLast.getTime());

        if (locationInfo.lastLat != 0 && locationInfo.lastLong != 0) {
            if (lastLat != locationInfo.lastLat && lastLong != locationInfo.lastLong) {
                return true;
            } else if (diffMinute >= 0) {
                return true;
            }
        }
        return false;
    }
}
