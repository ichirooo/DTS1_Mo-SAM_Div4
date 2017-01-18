package co.id.datascrip.mo_sam_div4_dts1.process.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IC_Customer {

    String URL_FIND_CUSTOMER = "customer_find",
            URL_GET_NO_CUSTOMER = "customer_get_no",
            URL_SAVE_CUSTOMER = "customer_save";

    @FormUrlEncoded
    @POST(URL_FIND_CUSTOMER)
    Call<ResponseBody> Find(@Field("sistem") String sistem, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_GET_NO_CUSTOMER)
    Call<ResponseBody> Get_No(@Field("json") String json);

    @FormUrlEncoded
    @POST(URL_SAVE_CUSTOMER)
    Call<ResponseBody> Post(@Field("json") String json);
}
