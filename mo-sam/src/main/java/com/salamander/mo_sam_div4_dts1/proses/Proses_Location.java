package com.salamander.mo_sam_div4_dts1.proses;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Function;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Kegiatan;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Location;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_location.LocationInfo;
import com.salamander.salamander_network.RetroData;
import com.salamander.salamander_network.RetroResp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Proses_Location {

    private Context context;
    private ProgressDialog progressDialog;
    private IC_Location IC;

    public Proses_Location(Context context) {
        this.context = context;
        this.IC = App.createRetrofit(context).create(IC_Location.class);
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void Send_Position(final Context context, final String id_kegiatan, final String bex_initial, final String bex_no, final String latitude, final String longitude) {
        IC_Kegiatan IC = App.createRetrofit(context).create(IC_Kegiatan.class);
        final String json = makeJSON(id_kegiatan, bex_initial, bex_no, latitude, longitude);
        IC.SendPosition(json).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Send_Position(context, id_kegiatan, bex_initial, bex_no, latitude, longitude);
            }
        });
    }

    public void PostLocation(@NonNull final Kegiatan kegiatan, final int check_in, final Callbacks.onCBLocation CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                PostLocation(kegiatan, check_in, CB);
            }
        })) {
            Location currentLocation = App.getSession(context).getLastLocation();
            if ((currentLocation.getLatitude() == 0 && currentLocation.getLongitude() == 0)) {
                RetroResp.dismissDialog(progressDialog);
                DialogUtils.showErrorMessage(context, "Unable to retrieve location. \nPlease wait a moment and try again.", false);
            } else {
                final String json = makeJSON(kegiatan, check_in);
                IC.PostLocation(App.getUser(context).getEmpNo(), json).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
                    @Override
                    public void onCall(RetroData retroData) {
                        super.onCall(retroData);
                        if (retroData.isSuccess()) {
                            kegiatan.setCheckedIn(check_in);
                            if (check_in == 1) {
                                App.getSession(context).setCheckedInKegiatan(kegiatan.getIDServer());
                            } else App.getSession(context).setCheckedInKegiatan(0);
                            new KegiatanSQLite(context).Post(kegiatan);
                        }
                        CB.onCB(retroData.getRetroStatus(), kegiatan);
                        RetroResp.dismissDialog(progressDialog);
                    }
                });
            }
        } else {
            RetroResp.dismissDialog(progressDialog);
        }
    }

    private String makeJSON(Kegiatan kegiatan, int check_in) {
        assert kegiatan != null;
        int id_kegiatan = kegiatan.getIDServer();

        try {
            JSONObject jsonObject = new JSONObject();
            Location currentLocation = App.getSession(context).getLastLocation();
            String tipe;
            if (check_in == 1) {
                tipe = "IN";
            } else tipe = "OUT";

            jsonObject.put(Kegiatan.KEGIATAN_ID_SERVER, id_kegiatan);
            jsonObject.put(User.BEX, App.getSession(context).getUserLogin().getEmpNo());
            jsonObject.put("Tipe", tipe);
            jsonObject.put("Waktu", DateUtils.dateToString(Tanggal.FORMAT_DATETIME_FULL, new Date()));
            //jsonObject.put("Timezone", App.getTimeZone());
            jsonObject.put("Latitude", currentLocation.getLatitude());
            jsonObject.put("Longitude", currentLocation.getLongitude());
            jsonObject.put("Timestamp", DateUtils.dateToString(Tanggal.FORMAT_DATETIME_FULL, new Date(currentLocation.getTime())));

            JSONArray jsonArray = new JSONArray();
            //for (String app : App.getListFakeGPSApp(context)) {
            //    jsonArray.put(app);
            //}
            jsonObject.put("fake_app", jsonArray);
            return jsonObject.toString();
        } catch (Exception e) {
            //FileUtil.writeExceptionLog(context, Proses_Location.class.getSimpleName() + " => makeJSON([kegiatan = " + String.valueOf(id_kegiatan) + ", check_in = " + String.valueOf(check_in) + "]) => " + e.getClass().getSimpleName(), e);
            return null;
        }
    }

    private String makeJSON(String id_kegiatan, String bex_initial, String bex_no, String latitude, String longitude) {
        JSONObject json = new JSONObject();
        try {
            json.put(Kegiatan.KEGIATAN_ID_SERVER, id_kegiatan);
            json.put(LocationInfo.POSITION_LATITUDE, latitude);
            json.put(LocationInfo.POSITION_LONGITUDE, longitude);
            json.put(User.BEX_INITIAL, bex_initial);
            json.put(User.BEX_EMP_NO, bex_no);
        } catch (JSONException e) {
            new Function().writeToText("position_json", e.toString());
        }
        return json.toString();
    }
}
