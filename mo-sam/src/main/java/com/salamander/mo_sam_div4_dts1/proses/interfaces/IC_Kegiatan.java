package com.salamander.mo_sam_div4_dts1.proses.interfaces;

import com.salamander.salamander_network.RetroData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface IC_Kegiatan {

    String URL_LOGIN = "login",
            URL_POST_KEGIATAN = "kegiatan_post",
            URL_CANCEL = "kegiatan_cancel",
            URL_KETERANGAN = "kegiatan_keterangan",
            URL_RESULT = "kegiatan_result",
            URL_CHECK_IN_OUT = "kegiatan_check_in_out",
            URL_SYNC = "kegiatan_sync",
            URL_CHECK_IN = "kegiatan_check_in",
            URL_CHECK_OUT = "kegiatan_check_out",
            URL_REFRESH_TODAY = "kegiatan_refresh_today",
            URL_REFRESH_DATE = "kegiatan_refresh_date",
            URL_SEND_POSITION = "send_position";

    @FormUrlEncoded
    @POST(URL_LOGIN)
    Call<ResponseBody> Login(@Field("json") String json);

    @FormUrlEncoded
    @POST(URL_POST_KEGIATAN)
    Call<ResponseBody> Post(@Field("bex") int bex, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_CANCEL)
    Call<ResponseBody> Cancel(@Field("bex") int bex, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_RESULT)
    Call<ResponseBody> Result(@Field("bex") int bex, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_KETERANGAN)
    Call<ResponseBody> Keterangan(@Field("bex") int bex, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_CHECK_IN)
    Call<ResponseBody> CheckIn(@Field("bex") int bex, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_CHECK_OUT)
    Call<ResponseBody> CheckOut(@Field("bex") int bex, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_SYNC)
    Call<RetroData> Sync(@Field("bex") int bex, @Field("initial") String initial, @Field("startDate") String startDate, @Field("endDate") String endDate);

    @FormUrlEncoded
    @POST(URL_REFRESH_TODAY)
    Call<ResponseBody> RefreshToday(@Field("sistem") String sistem, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_REFRESH_DATE)
    Call<ResponseBody> RefreshDate(@Field("sistem") String sistem, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_SEND_POSITION)
    Call<ResponseBody> SendPosition(@Field("json") String json);

}
