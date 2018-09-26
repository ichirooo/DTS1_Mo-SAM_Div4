package com.salamander.salamander_network;

import android.app.ProgressDialog;
import android.content.Context;

import com.salamander.salamander_base_module.Utils;

import java.net.URLDecoder;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetroResp {

    public static void dismissDialog(ProgressDialog progressDialog) {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public static abstract class BaseCallBack<T> {
        public abstract void onCall(RetroData retroData);
        //public abstract void onFailure(RetroData retroData);
    }

    public static abstract class SuccessCallback<T> extends BaseCallBack<T> implements Callback<T> {

        private Context context;
        private RetroData retroData = new RetroData();

        protected SuccessCallback(Context context) {
            setRetroData(context, null);
        }

        /*
        protected SuccessCallback(Context context, RetroData retroData) {
            setRetroData(context, retroData.getClassName(), retroData.getClassName(), retroData.getParameter());
        }

        protected SuccessCallback(Context context, String className, String methodName, String parameter) {
            setRetroData(context, className, methodName, parameter);
        }
        */

        protected SuccessCallback(Context context, String parameter) {
            setRetroData(context, parameter);
        }

        private void setRetroData(Context context, String parameter) {
            this.context = context;
            StackTraceElement[] stackTraceElementList = Thread.currentThread().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElementList) {
                String packageName = context.getApplicationContext().getPackageName();
                if (stackTraceElement.toString().contains(packageName) && !stackTraceElement.toString().contains("<init>")) {
                    retroData.setClassName(stackTraceElement.getClassName());
                    retroData.setMethodName(stackTraceElement.getMethodName());
                    retroData.setParameter(parameter);
                    break;
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            Response<ResponseBody> responseBody = null;

            if (call.request().body() instanceof FormBody) {
                FormBody formBody = ((FormBody) call.request().body());
                if (Utils.isEmpty(retroData.getParameter()) && formBody != null) {
                    String parameter = "";
                    int paramSize = formBody.size();
                    for (int i = 0; i < paramSize; i++)
                        parameter = parameter + formBody.name(i) + " => " +
                                URLDecoder.decode(formBody.value(i)) + "; ";
                    retroData.setParameter(parameter);
                }
            }

            if (response.body() instanceof RetroData)
                retroData = (RetroData) response.body();
            else {
                responseBody = (Response<ResponseBody>) response;
                retroData.setResult(Retro.getString(responseBody));
            }

            RetroStatus status = Retro.getRetroStatus(responseBody, retroData.getResult());
            status.setHeader(response.raw().toString());
            status.setTitle(response.raw().message());
            status.setStatusCode(response.code());
            retroData.setRetroStatus(status);

            onCall(retroData);
        }

        @Override
        public void onFailure(Call<T> call, Throwable throwable) {
            RetroStatus status = Retro.getRetroStatus(null, throwable.getMessage());
            if (!Retro.isConnected(context))
                status.setMessage("Not connected to internet.\nCheck your connection and try again");
            status.setHeader(throwable.getClass().getSimpleName());
            retroData.setRetroStatus(status);
            onCall(retroData);
        }

        @Override
        public void onCall(RetroData retroData) {
            //FileUtil.writeNetworkLog(context, className + " => " + methodName + " => onCall([RetroStatus])", parameter, status);
        }
    }
}
