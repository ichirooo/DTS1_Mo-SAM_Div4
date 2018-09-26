package com.salamander.mo_sam_div4_dts1.proses;

import android.app.ProgressDialog;
import android.content.Context;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Function;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.object.Feedback;
import com.salamander.mo_sam_div4_dts1.object.Log_Feed;
import com.salamander.mo_sam_div4_dts1.proses.callback.CallbackFeedback;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Feedback;
import com.salamander.mo_sam_div4_dts1.sqlite.LogSQLite;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Proses_Feedback {

    private Context context;
    private ProgressDialog progressDialog;

    public Proses_Feedback(Context context) {
        this.context = context;
    }

    public Proses_Feedback(Context context, String message) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void LoadFeedback(final CallbackFeedback.CBLoad CB) {
        IC_Feedback IC = App.createRetrofit(context).create(IC_Feedback.class);
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
        IC_Feedback IC = App.createRetrofit(context).create(IC_Feedback.class);
        IC.SendReply(makeJSON(feedback)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Feedback->Send_Reply", result);
                if (result != null && !result.isEmpty() && result.contains(context.getString(R.string.success))) {
                    Log_Feed log = new Log_Feed();
                    log.setFeedback(feedback);
                    log.setLogDate(new Date().getTime());
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
            json_feed.put("bex", feedback.getUser().getEmpNo());
            json_feed.put("line", feedback.getLineID());
            json_feed.put("note", feedback.getNote());
            json.put("bex", App.getUser(context).getInitial());
            json.put("data", json_feed);
        } catch (JSONException e) {
            new Function(context).writeToText("Send Reply -> makeJSON", e.toString());
        }
        return json.toString();
    }

    private String makeJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("bex_no", App.getUser(context).getEmpNo());
            json.put("bex", App.getUser(context).getInitial());
        } catch (JSONException e) {
            new Function(context).writeToText("Load Feedback -> makeJSON", e.toString());
        }
        return json.toString();
    }

}
