package co.id.datascrip.mo_sam_div4_dts1.process;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.process.interfaces.IC_Kegiatan;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Process_Send_Position {

    public void Send_Position(final Context context, final String id_kegiatan, final String bex_initial, final String bex_no, final String latitude, final String longitude) {
        IC_Kegiatan IC = Global.CreateRetrofit(context).create(IC_Kegiatan.class);
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
}
