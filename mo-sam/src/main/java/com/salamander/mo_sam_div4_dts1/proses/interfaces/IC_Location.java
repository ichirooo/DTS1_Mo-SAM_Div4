package com.salamander.mo_sam_div4_dts1.proses.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IC_Location {

    String URL_LOCATION = "post_location";
    String URL_REPORT_FAKE_LOCATION = "report_fake_location";

    @FormUrlEncoded
    @POST(URL_LOCATION)
    Call<ResponseBody> PostLocation(@Field("bex") int bex, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_REPORT_FAKE_LOCATION)
    Call<ResponseBody> ReportFakeLocation(@Field("bex") int bex, @Field("json") String json);
}