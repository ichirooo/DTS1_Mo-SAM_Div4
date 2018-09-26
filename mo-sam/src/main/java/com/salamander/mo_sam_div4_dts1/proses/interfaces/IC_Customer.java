package com.salamander.mo_sam_div4_dts1.proses.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IC_Customer {

    String URL_FIND_CUSTOMER = "customer_find",
            URL_GET_NO_CUSTOMER = "customer_get_no",
            URL_SAVE_CUSTOMER = "customer_post",
            URL_CUSTOMER_GET_ALL = "customer_get_all";

    @FormUrlEncoded
    @POST(URL_FIND_CUSTOMER)
    Call<ResponseBody> find(@Field("bex") int bex, @Field("code") String code);

    @FormUrlEncoded
    @POST(URL_GET_NO_CUSTOMER)
    Call<ResponseBody> generateCode(@Field("bex") int bex, @Field("initial") String initial);

    @FormUrlEncoded
    @POST(URL_SAVE_CUSTOMER)
    Call<ResponseBody> post(@Field("bex") int bex, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_CUSTOMER_GET_ALL)
    Call<ResponseBody> getAll(@Field("bex") int bex, @Field("initial") String initial);

}
