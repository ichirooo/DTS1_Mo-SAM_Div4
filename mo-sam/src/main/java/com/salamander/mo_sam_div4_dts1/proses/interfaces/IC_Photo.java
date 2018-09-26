package com.salamander.mo_sam_div4_dts1.proses.interfaces;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by benny_aziz on 09/19/2016.
 */
public interface IC_Photo {

    String URL_PHOTO = "photo.php";
    String URL_PHOTO_UPLOAD = "photo_upload";
    String URL_PHOTO_UPLOAD_ALL = "photo_upload_all";
    String URL_PHOTO_NOTE = "photo_description";
    String URL_PHOTO_DELETE = "photo_delete";

    @FormUrlEncoded
    @POST(URL_PHOTO_DELETE)
    Call<ResponseBody> Delete(@Field("bex") int bex, @Field("json") String json);

    @Multipart
    @POST(URL_PHOTO_UPLOAD)
    Call<ResponseBody> Upload(
            @Part("bex") RequestBody user,
            @Part("json") RequestBody json,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(URL_PHOTO_NOTE)
    Call<ResponseBody> Note(
            @Field("bex") int bex,
            @Field("json") String json);

}