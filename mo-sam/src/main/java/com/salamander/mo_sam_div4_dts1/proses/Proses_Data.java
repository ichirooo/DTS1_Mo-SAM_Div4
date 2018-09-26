package com.salamander.mo_sam_div4_dts1.proses;

import android.app.ProgressDialog;
import android.content.Context;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Get_Data;
import com.salamander.salamander_network.JSON;
import com.salamander.salamander_network.RetroData;
import com.salamander.salamander_network.RetroResp;

import org.json.JSONObject;

import okhttp3.ResponseBody;

public class Proses_Data {

    private Context context;
    private ProgressDialog progressDialog;
    private IC_Get_Data IC;

    public Proses_Data(Context context, boolean showDialog) {
        this.context = context;
        this.IC = App.createRetrofit(context).create(IC_Get_Data.class);
        if (showDialog) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @SuppressWarnings("unchecked")
    public void getData(final Callbacks.OnCB CB) {
        //if (App.isConnectedToInternet(context)) {
        IC.getData().enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                RetrofitResponse.getData(context, retroData.getResult());
                CB.onCB(retroData.getRetroStatus());
                dismissDialog();
            }
        });
    }

    public void getHoliday(final Callbacks.OnCB CB) {
        //if (App.isConnectedToInternet(context)) {
        IC.getData().enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                CB.onCB(retroData.getRetroStatus());
                dismissDialog();
            }
        });
    }

    public void getTermsOfPayment(final Callbacks.OnCB CB) {
        //if (App.isConnectedToInternet(context)) {
        IC.getData().enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                CB.onCB(retroData.getRetroStatus());
                dismissDialog();
            }
        });
    }

    public void getBlackListApp(final Callbacks.OnCB CB) {
        //if (App.isConnectedToInternet(context)) {
        IC.getData().enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                CB.onCB(retroData.getRetroStatus());
                dismissDialog();
            }
        });
    }

    public void checkVersion(final Callbacks.OnCBCheckVersion CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                checkVersion(CB);
            }
        }))
            IC.checkVersion(App.getUser(context).getEmpNo(), App.getVersion(context).trim()).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
                @Override
                public void onCall(RetroData retroData) {
                    super.onCall(retroData);
                    JSONObject jsonObject = JSON.toJSONObject(retroData.getResult());
                    if (retroData.isSuccess())
                        RetrofitResponse.getData(context, retroData.getResult());
                    CB.onCB(retroData.getRetroStatus(), JSON.getString(jsonObject, "version"), JSON.getString(jsonObject, "link"));
                }
            });
    }
/*
    public void sendLog(final File logFile, final File dbFile, final Callbacks.OnCB CB) {
        if (App.isConnectedToInternet(context)) {

            RequestBody requestLogFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), logFile);
            RequestBody requestDBFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), dbFile);
            MultipartBody.Part body_log = MultipartBody.Part.createFormData("file", logFile.getName(), requestLogFile);
            MultipartBody.Part body_db = MultipartBody.Part.createFormData("db", dbFile.getName(), requestDBFile);
            RequestBody bex = createPartFromString(String.valueOf(App.getUser(context).getEmpNo()));
            final RequestBody json = createPartFromString(logFile.getName());

            IC.sendLog(bex, json, body_log, body_db).enqueue(new Callbacks<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String respon = Retro.getString(response);
                    FileUtil.writeNetworkLog(context, Proses_Data.class.getSimpleName() + " => sendLog => onResponse", null, response, logFile.getName());
                    CB.onCB(Retro.getStatus(response, respon));
                    dismissDialog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    FileUtil.writeNetworkLog(context, Proses_Data.class.getSimpleName() + " => sendLog => onFailure", logFile.getName(), throwable);
                    dismissDialog();
                    CB.onCB(Retro.getStatus(null, throwable.getMessage()));
                    //FileDialogUtils.showErrorMessage(context, throwable.getMessage(), false);
                }
            });
        } else {
            dismissDialog();
        }
    }
*/

    private void dismissDialog() {
        try {
            if (progressDialog != null)
                progressDialog.dismiss();
        } catch (Exception e) {
            //Log.d(App.TAG, Proses_Data.class.getSimpleName() + " => dismissDialog  => " + e.toString());
        }
    }
}
