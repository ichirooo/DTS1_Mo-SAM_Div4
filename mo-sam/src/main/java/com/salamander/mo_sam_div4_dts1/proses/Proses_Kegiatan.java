package com.salamander.mo_sam_div4_dts1.proses;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Const;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.proses.converter.KegiatanConverter;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Kegiatan;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.salamander_network.JSON;
import com.salamander.salamander_network.RetroData;
import com.salamander.salamander_network.RetroResp;
import com.salamander.salamander_network.gson.GsonConverterFactory;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class Proses_Kegiatan {

    private Context context;
    private ProgressDialog progressDialog;
    private IC_Kegiatan IC;
    private KegiatanSQLite kegiatanSQLite;

    public Proses_Kegiatan(Context context) {
        this.context = context;
        IC = App.createRetrofit(context).create(IC_Kegiatan.class);
        kegiatanSQLite = new KegiatanSQLite(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void post(final Kegiatan kegiatan, final Callbacks.OnCBKegiatan CB) {
        IC.Post(App.getUser(context).getEmpNo(), kegiatan.getAsJSONPost()).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                if (retroData.isSuccess()) {
                    kegiatan.setIDServer(JSON.getInt(JSON.toJSONObject(retroData.getResult()), Kegiatan.KEGIATAN_ID_SERVER));
                    kegiatan.getSalesHeader().getCustomer().setIDKegiatan(kegiatan.getIDServer());
                    kegiatanSQLite.Post(kegiatan);
                }
                CB.onCB(retroData.getRetroStatus(), kegiatan);
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void cancel(final Kegiatan kegiatan, final Callbacks.OnCB CB) {
        IC.Cancel(App.getUser(context).getEmpNo(), makeJSONCancel(kegiatan)).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                if (retroData.isSuccess()) {
                    kegiatanSQLite.Post(kegiatan);
                }
                CB.onCB(retroData.getRetroStatus());
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void result(final Kegiatan kegiatan, final Callbacks.OnCB CB) {
        IC.Result(App.getUser(context).getEmpNo(), makeJSONResult(kegiatan)).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                if (retroData.isSuccess()) {
                    kegiatanSQLite.Post(kegiatan);
                }
                CB.onCB(retroData.getRetroStatus());
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void keterangan(final Kegiatan kegiatan, final Callbacks.OnCB CB) {
        IC.Keterangan(App.getUser(context).getEmpNo(), makeJSONKeterangan(kegiatan)).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                if (retroData.isSuccess()) {
                    kegiatanSQLite.Post(kegiatan);
                }
                CB.onCB(retroData.getRetroStatus());
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void checkIn(final Kegiatan kegiatan, final Callbacks.OnCB CB) {
        kegiatan.setCheckedIn(1);
        IC.CheckIn(App.getUser(context).getEmpNo(), makeJSONCheckInOut(kegiatan)).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                if (retroData.isSuccess()) {
                    kegiatanSQLite.checkIn(kegiatan.getIDServer());
                }
                CB.onCB(retroData.getRetroStatus());
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void checkOut(final Kegiatan kegiatan, final Callbacks.OnCB CB) {
        kegiatan.setCheckedIn(0);
        IC.CheckOut(App.getUser(context).getEmpNo(), makeJSONCheckInOut(kegiatan)).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                if (retroData.isSuccess()) {
                    kegiatanSQLite.checkOut(kegiatan.getIDServer());
                }
                CB.onCB(retroData.getRetroStatus());
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void syncKegiatan(String tglMulai, String tglSelesai, final Callbacks.OnCB CB) {
        final String parameter = "tglMulai = " + tglMulai + "; tglSelesai = " + tglSelesai;
        final ArrayList<Integer> list_id_kegiatan = new KegiatanSQLite(context).getAllIDAsArray(tglMulai, tglSelesai);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RetroData.class, new KegiatanConverter.SyncKegiatanConverter(context, list_id_kegiatan));
        Gson gson = gsonBuilder.create();

        IC = App.createRetrofit(context, GsonConverterFactory.create(gson)).create(IC_Kegiatan.class);
        IC.Sync(App.getUser(context).getEmpNo(), App.getUser(context).getInitial(), tglMulai, tglSelesai).enqueue(new RetroResp.SuccessCallback<RetroData>(context, parameter) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                CB.onCB(retroData.getRetroStatus());
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    private String makeJSONCheckInOut(Kegiatan kegiatan) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Kegiatan.KEGIATAN_ID_SERVER, kegiatan.getIDServer());
            jsonObject.put(User.BEX, kegiatan.getSalesperson().getEmpNo());
            jsonObject.put(Kegiatan.KEGIATAN_CHECKED_IN, kegiatan.getCheckedIn());
            Location location = App.getSession(context).getLastLocation();
            jsonObject.put(Const.CAPTION_LATITUDE, location.getLatitude());
            jsonObject.put(Const.CAPTION_LONGITUDE, location.getLongitude());
        } catch (Exception e) {
            //FileUtil.writeExceptionLog(context, Proses_Kegiatan.class.getSimpleName() + " => makeJSONCancel => ", e);
            Log.d(App.TAG, Proses_Kegiatan.class.getSimpleName() + " => makeJSONCheckInOut  => " + e.toString());
        }
        return jsonObject.toString();
    }

    private String makeJSONCancel(Kegiatan kegiatan) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Kegiatan.KEGIATAN_ID_SERVER, kegiatan.getIDServer());
            jsonObject.put(Kegiatan.KEGIATAN_CANCELED, kegiatan.getCancel());
            jsonObject.put(Kegiatan.KEGIATAN_CANCEL_REASON, kegiatan.getCancelReason());
            jsonObject.put(Kegiatan.KEGIATAN_CANCEL_DATE, kegiatan.getCancelDate().getTglString());
        } catch (Exception e) {
            //FileUtil.writeExceptionLog(context, Proses_Kegiatan.class.getSimpleName() + " => makeJSONCancel => ", e);
            Log.d(App.TAG, Proses_Kegiatan.class.getSimpleName() + " => makeJSONCancel  => " + e.toString());
        }
        return jsonObject.toString();
    }

    private String makeJSONResult(Kegiatan kegiatan) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Kegiatan.KEGIATAN_ID_SERVER, kegiatan.getIDServer());
            jsonObject.put(Kegiatan.KEGIATAN_RESULT, kegiatan.getResult());
            jsonObject.put(Kegiatan.KEGIATAN_RESULT_DATE, kegiatan.getResultDate().getTglString());
        } catch (Exception e) {
            //FileUtil.writeExceptionLog(context, Proses_Kegiatan.class.getSimpleName() + " => makeJSONHasil => ", e);
            Log.d(App.TAG, Proses_Kegiatan.class.getSimpleName() + " => makeJSONResult  => " + e.toString());
        }
        return jsonObject.toString();
    }

    private String makeJSONKeterangan(Kegiatan kegiatan) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Kegiatan.KEGIATAN_ID_SERVER, kegiatan.getIDServer());
            jsonObject.put(Kegiatan.KEGIATAN_KETERANGAN, kegiatan.getKeterangan());
        } catch (Exception e) {
            //FileUtil.writeExceptionLog(context, Proses_Kegiatan.class.getSimpleName() + " => makeJSONHasil => ", e);
            Log.d(App.TAG, Proses_Kegiatan.class.getSimpleName() + " => makeJSONKeterangan  => " + e.toString());
        }
        return jsonObject.toString();
    }

/*
    public void Action(final Kegiatan kegiatan, final String action, final CallbackKegiatan.CBAction CB) {
        IC_Kegiatan IC = App.createRetrofit(context).create(IC_Kegiatan.class);
        String json = makeJSON(action, kegiatan);
        switch (action) {
            case ACTION_CANCEL:
                IC.Cancel(json).enqueue(new Callbacks<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->Action->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).Cancel(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->Action->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
            case ACTION_KETERANGAN:
                IC.Keterangan(json).enqueue(new Callbacks<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->Action->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).Keterangan(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->Action->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
            case ACTION_RESULT:
                IC.Result(json).enqueue(new Callbacks<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->Action->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).Result(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->Action->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
        }
    }

    public void CheckInCheckOut(final Kegiatan kegiatan, final String action, final CallbackKegiatan.CBCheckInCheckOut CB) {
        IC_Kegiatan IC = App.createRetrofit(context).create(IC_Kegiatan.class);
        String json = makeJSON(action, kegiatan);
        switch (action) {
            case ACTION_CHECK_IN:
                IC.CheckIn(SISTEM, json).enqueue(new Callbacks<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).update(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
            case ACTION_CHECK_OUT:
                IC.CheckOut(json).enqueue(new Callbacks<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).update(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
        }
    }

    public void Refresh(final String action, final String date, final CallbackKegiatan.CBRefresh CB) {
        IC_Kegiatan IC = App.createRetrofit(context).create(IC_Kegiatan.class);
        String json = makeJSON(action, date);
        switch (action) {
            case ACTION_REFRESH_TODAY:
                IC.RefreshToday(SISTEM, json).enqueue(new Callbacks<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, result);
                        if (RetrofitResponse.isSuccess(result)) {
                            ArrayList<Kegiatan> list_kegiatan = RetrofitResponse.getListKegiatan(result);
                            new KegiatanSQLite(context).setStatus(list_kegiatan);
                            CB.onCB(list_kegiatan);
                        }
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
            case ACTION_REFRESH_DATE:
                IC.RefreshDate(SISTEM, json).enqueue(new Callbacks<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, result);
                        if (RetrofitResponse.isSuccess(result)) {
                            ArrayList<Kegiatan> list_kegiatan = RetrofitResponse.getListKegiatan(result);
                            new KegiatanSQLite(context).setStatus(list_kegiatan);
                            CB.onCB(list_kegiatan);
                        }
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
        }
    }

    private String makeJSON(Kegiatan kegiatan) {
        JSONObject json_kegiatan = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json_kegiatan.put("cust_code", kegiatan.getSalesHeader().getCustomer().getCode());
            if (!kegiatan.getSalesHeader().getCustomer().getCode().contains("CUST"))
                json_kegiatan.put("cust_nav", kegiatan.getSalesHeader().getCustomer().getCode());
            json_kegiatan.put("cust_name", kegiatan.getSalesHeader().getCustomer().getName());
            json_kegiatan.put("lat", kegiatan.getSalesHeader().getCustomer().getLatitude());
            json_kegiatan.put("lang", kegiatan.getSalesHeader().getCustomer().getLongitude());

            json_kegiatan.put("bex_no", kegiatan.getSalesperson().getEmpNo());
            json_kegiatan.put("bex_initial", kegiatan.getSalesperson().getInitial());

            json_kegiatan.put("id", kegiatan.getIDServer());
            json_kegiatan.put("startDate", kegiatan.getStartDate());
            json_kegiatan.put("endDate", kegiatan.getEndDate());
            json_kegiatan.put("tipe", kegiatan.getTipe());
            json_kegiatan.put("jenis", kegiatan.getJenis());
            json_kegiatan.put("subject", kegiatan.getSubject());
            json_kegiatan.put("cancel", kegiatan.getCancel());
            json_kegiatan.put("cancel_reason", kegiatan.getCancelReason());
            json_kegiatan.put("keterangan", kegiatan.getKeterangan());
            json_kegiatan.put("result", kegiatan.getResult());
            json_kegiatan.put("resultDate", kegiatan.getResultDate());
            json_kegiatan.put("checkIn", kegiatan.getCheckedIn());

            json.put("bex", App.getUser(context).getInitial());
            json.put("data", json_kegiatan);

        } catch (Exception e) {
            return e.toString();
        }
        return json.toString();
    }

    private String makeJSON(String action, Kegiatan kegiatan) {
        JSONObject json = new JSONObject();
        JSONObject json_kegiatan = new JSONObject();
        try {
            switch (action) {
                case ACTION_CANCEL:
                    json_kegiatan.put("id", kegiatan.getIDServer());
                    json_kegiatan.put("cancel", kegiatan.getCancel());
                    json_kegiatan.put("reason", kegiatan.getCancelReason());
                    break;

                case ACTION_KETERANGAN:
                    json_kegiatan.put("id", kegiatan.getIDServer());
                    json_kegiatan.put("keterangan", kegiatan.getKeterangan());
                    break;

                case ACTION_RESULT:
                    json_kegiatan.put("id", kegiatan.getIDServer());
                    json_kegiatan.put("result", kegiatan.getResult());
                    break;

                case ACTION_CHECK_IN:
                    json_kegiatan.put("id", kegiatan.getIDServer());
                    break;

                case ACTION_CHECK_OUT:
                    json_kegiatan.put("id", kegiatan.getIDServer());
                    break;
            }
            json.put("bex", App.getUser(context).getInitial());
            json.put("data", json_kegiatan);
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }
        return json.toString();
    }

    private String makeJSON(String action, String date) {
        JSONObject json = new JSONObject();
        JSONObject json_kegiatan = new JSONObject();
        try {
            switch (action) {
                case ACTION_REFRESH_TODAY:
                    break;

                case ACTION_REFRESH_DATE:
                    json_kegiatan.put("date", date);
                    break;
            }
            json.put("bex", App.getUser(context).getInitial());
            json.put("data", json_kegiatan);
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }
        return json.toString();
    }
    */
}
