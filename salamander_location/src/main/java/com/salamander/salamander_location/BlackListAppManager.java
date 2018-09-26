package com.salamander.salamander_location;

import android.content.Context;

import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_network.Retro;
import com.salamander.salamander_network.RetroStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * Created by benny_aziz on 02/22/2018.
 */

public class BlackListAppManager {

    public static void getBlackListApp(Retrofit retrofit, final Context context, String url, final OnCB onCB) {
        IC_GetBlacklistApp IC = retrofit.create(IC_GetBlacklistApp.class);
        IC.getBlackListApp().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String respon = Retro.getString(response);
                if (Retro.isSuccess(response, respon)) {
                    try {
                        JSONObject jsonObject = new JSONObject(respon);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        SalamanderLocation.getLocationManager(context).setListBlacklistApp(jsonArray);
                    } catch (Exception e) {
                        //Log.e("ERROR->getBlacklistApp", e.toString());
                        Utils.showLog(BlackListAppManager.class.getSimpleName(), "getBlacklistApp => onResponse", e.toString());
                    }
                }
                if (onCB != null)
                    onCB.onCB(Retro.getRetroStatus(response, respon));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                //Log.e("ERROR->getBlacklistApp", throwable.getMessage());
                Utils.showLog(BlackListAppManager.class.getSimpleName(), "getBlacklistApp => onFailure", throwable.getMessage());
                if (onCB != null)
                    onCB.onCB(Retro.getRetroStatus(null, null));
            }
        });
    }

    public interface OnCB {
        void onCB(RetroStatus status);
    }

    public interface IC_GetBlacklistApp {
        @GET("get_blacklist_app")
        Call<ResponseBody> getBlackListApp();
    }
}
