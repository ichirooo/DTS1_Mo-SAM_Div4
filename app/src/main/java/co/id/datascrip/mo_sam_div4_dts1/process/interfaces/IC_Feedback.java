package co.id.datascrip.mo_sam_div4_dts1.process.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IC_Feedback {

    String URL_LOAD_FEEDBACK = "feedback_load",
            URL_SEND_REPLY = "feedback_send";

    @FormUrlEncoded
    @POST(URL_LOAD_FEEDBACK)
    Call<ResponseBody> LoadFeedback(@Field("json") String json);

    @FormUrlEncoded
    @POST(URL_SEND_REPLY)
    Call<ResponseBody> SendReply(@Field("json") String json);

}
