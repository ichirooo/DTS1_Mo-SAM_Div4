package com.salamander.mo_sam_div4_dts1.proses.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IC_Get_Data {

    @GET("get_data")
    Call<ResponseBody> getData();

    @FormUrlEncoded
    @POST("check_version")
    Call<ResponseBody> checkVersion(@Field("bex") int bex_no, @Field("version") String currentVersion);

}