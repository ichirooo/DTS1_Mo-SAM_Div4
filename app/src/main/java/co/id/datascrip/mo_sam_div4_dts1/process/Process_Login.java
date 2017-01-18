package co.id.datascrip.mo_sam_div4_dts1.process;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackLogin;
import co.id.datascrip.mo_sam_div4_dts1.object.BEX;
import co.id.datascrip.mo_sam_div4_dts1.process.interfaces.IC_Kegiatan;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Process_Login {

    private Context context;
    private ProgressDialog progressDialog;

    public Process_Login(Context context) {
        this.context = context;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Logging In...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void Login(String username, String password, final CallbackLogin.CBLogin CB) {
        IC_Kegiatan IC = Global.CreateRetrofit(context).create(IC_Kegiatan.class);
        IC.Login(makeJSON(username, password)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Login->Login", result);
                BEX bex = new BEX();
                String datetime = null;
                if (result != null && !result.isEmpty()) {
                    try {
                        JSONArray ja = new JSONArray(result);
                        JSONObject jo = ja.getJSONObject(0);
                        bex.setEmpNo(jo.getString("ID"));
                        bex.setInitial(jo.getString("I"));
                        bex.setFullname(jo.getString("F"));
                        bex.setPhoto(jo.getString("A"));
                        datetime = jo.getString("d");
                    } catch (JSONException e) {
                        bex = null;
                    }
                } else bex.setInitial("null");
                CB.onCB(bex, datetime);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Login->Login", throwable.getMessage());
                progressDialog.dismiss();
                CB.onCB(null, null);
            }
        });
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
