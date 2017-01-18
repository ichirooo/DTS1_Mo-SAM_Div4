package co.id.datascrip.mo_sam_div4_dts1.process;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackFeedback;
import co.id.datascrip.mo_sam_div4_dts1.object.Feedback;
import co.id.datascrip.mo_sam_div4_dts1.object.Log_Feed;
import co.id.datascrip.mo_sam_div4_dts1.process.interfaces.IC_Feedback;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.LogSQLite;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Process_Feedback {

    private Context context;
    private ProgressDialog progressDialog;

    public Process_Feedback(Context context) {
        this.context = context;
    }

    public Process_Feedback(Context context, String message) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void LoadFeedback(final CallbackFeedback.CBLoad CB) {
        IC_Feedback IC = Global.CreateRetrofit(context).create(IC_Feedback.class);
        IC.LoadFeedback(makeJSON()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Feedback->LoadFeedback", result);
                ArrayList<Feedback> list_feedback = RetrofitResponse.getListFeedback(context, result);
                CB.onCB(list_feedback);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Feedback->LoadFeedback", throwable.getMessage());
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

    public void Send_Reply(final Feedback feedback, final CallbackFeedback.CBPost CB) {
        IC_Feedback IC = Global.CreateRetrofit(context).create(IC_Feedback.class);
        IC.SendReply(makeJSON(feedback)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Feedback->Send_Reply", result);
                if (result != null && !result.isEmpty() && result.contains(context.getString(R.string.success))) {
                    Log_Feed log = new Log_Feed();
                    log.setFeedback(feedback);
                    log.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                    new LogSQLite(context).Insert(log);
                    result = feedback.getNote();
                } else result = null;
                CB.onCB(result);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Feedback->Send_Reply", throwable.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private String makeJSON(Feedback feedback) {
        JSONObject json = new JSONObject();
        JSONObject json_feed = new JSONObject();
        try {
            json_feed.put("bex", feedback.getBex().getEmpNo());
            json_feed.put("line", feedback.getLineID());
            json_feed.put("note", feedback.getNote());
            json.put("bex", Global.getBEX(context).getInitial());
            json.put("data", json_feed);
        } catch (JSONException e) {
            new Function(context).writeToText("Send Reply -> makeJSON", e.toString());
        }
        return json.toString();
    }

    private String makeJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("bex_no", Global.getBEX(context).getEmpNo());
            json.put("bex", Global.getBEX(context).getInitial());
        } catch (JSONException e) {
            new Function(context).writeToText("Load Feedback -> makeJSON", e.toString());
        }
        return json.toString();
    }

}
