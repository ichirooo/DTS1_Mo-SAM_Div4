package co.id.datascrip.mo_sam_div4_dts1.process.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface IC_Order {

    String URL_GET_NO = "order_get_no",
            URL_GET_PRICE = "order_get_price",
            URL_CHECK_STATUS = "order_check_status",
            URL_FIND_ITEM = "order_find_item",
            URL_POST = "order_post";

    @FormUrlEncoded
    @POST(URL_GET_NO)
    Call<ResponseBody> GetNo(@Field("sistem") String sistem, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_GET_PRICE)
    Call<ResponseBody> GetPrice(@Field("sistem") String sistem, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_CHECK_STATUS)
    Call<ResponseBody> CheckStatus(@Field("json") String json);

    @FormUrlEncoded
    @POST(URL_FIND_ITEM)
    Call<ResponseBody> FindItem(@Field("sistem") String sistem, @Field("json") String json);

    @FormUrlEncoded
    @POST(URL_POST)
    Call<ResponseBody> Post(@Field("sistem") String sistem, @Field("json") String json);
}
