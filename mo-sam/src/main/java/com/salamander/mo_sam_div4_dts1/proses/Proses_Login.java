package com.salamander.mo_sam_div4_dts1.proses;

import android.app.ProgressDialog;
import android.content.Context;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.mo_sam_div4_dts1.proses.callback.CallbackLogin;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Kegiatan;
import com.salamander.salamander_network.JSON;
import com.salamander.salamander_network.RetroData;
import com.salamander.salamander_network.RetroResp;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;

public class Proses_Login {

    private Context context;
    private ProgressDialog progressDialog;

    public Proses_Login(Context context) {
        this.context = context;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Logging In...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void Login(String username, String password, final CallbackLogin.CBLogin CB) {
        IC_Kegiatan IC = App.createRetrofit(context).create(IC_Kegiatan.class);
        IC.Login(makeJSON(username, password)).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                JSONObject jsonObject = JSON.toJSONObject(retroData.getResult());
                CB.onCB(retroData.getRetroStatus(), new User().getFromJSON(retroData.getJSONData()), JSON.getString(jsonObject, "datetime"));
                RetroResp.dismissDialog(progressDialog);
            }
        });
        /*
        IC.Login(makeJSON(username, password)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Login->Login", result);
                User user = new User();
                String datetime = null;
                if (result != null && !result.isEmpty()) {
                    try {
                        JSONArray ja = new JSONArray(result);
                        JSONObject jo = ja.getJSONObject(0);
                        user.setEmpNo(jo.getInt("ID"));
                        user.setInitial(jo.getString("I"));
                        user.setFullname(jo.getString("F"));
                        user.setPhoto(jo.getString("A"));
                        datetime = jo.getString("d");
                    } catch (JSONException e) {
                        user = null;
                    }
                } else user.setInitial("null");
                CB.onCB(user, datetime);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Login->Login", throwable.getMessage());
                progressDialog.dismiss();
                CB.onCB(null, null);
            }
        });
        */
    }

    private String makeJSON(String username, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("user", username);
            json.put("pass", password);
        } catch (JSONException e) {
            // TODO: handle exception
            return e.toString();
        }
        return json.toString();
    }
}
