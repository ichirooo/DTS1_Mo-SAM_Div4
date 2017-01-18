package co.id.datascrip.mo_sam_div4_dts1.process.interfaces;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by benny_aziz on 09/19/2016.
 */
public interface IC_Photo {

    String URL_UPLOAD_PHOTO = "photo_upload";

    @Multipart
    @POST(URL_UPLOAD_PHOTO)
    Call<ResponseBody> Upload(
            @Part("count") RequestBody count,
            @Part("json") RequestBody json,
            @Part ArrayList<MultipartBody.Part> files);
}