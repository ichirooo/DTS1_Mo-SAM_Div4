package com.salamander.mo_sam_div4_dts1.proses;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.object.Photo;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Photo;
import com.salamander.mo_sam_div4_dts1.sqlite.PhotoSQLite;
import com.salamander.mo_sam_div4_dts1.util.FileUtil;
import com.salamander.salamander_network.JSON;
import com.salamander.salamander_network.ProgressRequestBody;
import com.salamander.salamander_network.RetroData;
import com.salamander.salamander_network.RetroResp;

import org.json.JSONObject;

import java.io.File;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class Proses_Photo {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private Context context;
    private ProgressDialog progressDialog;
    private IC_Photo IC;

    public Proses_Photo(Context context, boolean showLoading) {
        this.context = context;
        this.IC = App.createRetrofit(context).create(IC_Photo.class);
        progressDialog = new ProgressDialog(context);
        if (showLoading) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Uploading...");
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setProgressNumberFormat(null);
        } else {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void Delete(final Photo photo, final Callbacks.OnCB CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                Delete(photo, CB);
            }
        })) {
            final String parameter = makeJSONDelete(photo);
            IC.Delete(App.getUser(context).getEmpNo(), parameter).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
                @Override
                public void onCall(RetroData retroData) {
                    super.onCall(retroData);
                    CB.onCB(retroData.getRetroStatus());
                    RetroResp.dismissDialog(progressDialog);
                }
            });
        } else {
            dismissDialog();
        }
    }

    public void Upload(final Photo photo, final Callbacks.OnCB CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                Upload(photo, CB);
            }
        })) {
            MultipartBody.Part body = null;
            if (!photo.isPosted()) {
                File photoFile = new File(photo.getPath());
                File compressedPhoto;
                try {
                    compressedPhoto = new Compressor(context).setQuality(60).compressToFile(photoFile);
                } catch (Exception e) {
                    compressedPhoto = photoFile;
                }
                ProgressRequestBody requestFile = new ProgressRequestBody(compressedPhoto, new ProgressRequestBody.UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(final int percentage) {
                        progressDialog.setProgress(percentage);
                    }

                    @Override
                    public void onError() {
                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();
                    }
                });
                body = MultipartBody.Part.createFormData("file", photo.getName(), requestFile);
            }
            RequestBody user = createPartFromString(String.valueOf(App.getUser(context).getEmpNo()));
            final RequestBody json = createPartFromString(photo.getAsJSON().toString());
            IC.Upload(user, json, body).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
                @Override
                public void onCall(RetroData retroData) {
                    super.onCall(retroData);
                    if (retroData.isSuccess()) {
                        try {
                            PhotoSQLite photoSQLite = new PhotoSQLite(context);
                            JSONObject jsonObject = new JSONObject(retroData.getResult()).getJSONObject("data");
                            photo.setIDServer(JSON.getInt(jsonObject, Photo.PHOTO_ID_SERVER));
                            photo.setURL(JSON.getString(jsonObject, Photo.PHOTO_URL));
                            photoSQLite.Post(photo);
                        } catch (Exception e) {
                            FileUtil.writeExceptionLog(context, Proses_Photo.class.getSimpleName() + " => Upload => onResponse => " + e.getClass().getSimpleName(), e);
                            Log.d(App.TAG, Proses_Photo.class.getSimpleName() + " => onResponse  => " + e.toString());
                        }
                    }
                    CB.onCB(retroData.getRetroStatus());
                    RetroResp.dismissDialog(progressDialog);
                }
            });
        } else {
            dismissDialog();
        }
    }

    public void PostDescription(final Photo photo, final Callbacks.OnCB CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                PostDescription(photo, CB);
            }
        })) {
            final String parameter = makeJSONDescription(photo);
            IC.Note(App.getUser(context).getEmpNo(), parameter).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
                @Override
                public void onCall(RetroData retroData) {
                    super.onCall(retroData);
                    if (retroData.isSuccess())
                        new PhotoSQLite(context).Post(photo);
                    CB.onCB(retroData.getRetroStatus());
                    RetroResp.dismissDialog(progressDialog);
                }
            });
        } else {
            dismissDialog();
        }
    }

    private String makeJSONDelete(Photo photo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Photo.PHOTO_ID_SERVER, photo.getIDServer());
        } catch (Exception e) {
            FileUtil.writeExceptionLog(context, Proses_Photo.class.getSimpleName() + " => makeJSONDelete => ", e);
            Log.d(App.TAG, Proses_Photo.class.getSimpleName() + " => makeJSONDelete  => " + e.toString());
        }
        return jsonObject.toString();
    }

    private String makeJSONDescription(Photo photo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Photo.PHOTO_ID_SERVER, photo.getIDServer());
            jsonObject.put(Photo.PHOTO_DESCRIPTION, photo.getDescription());
        } catch (Exception e) {
            FileUtil.writeExceptionLog(context, Proses_Photo.class.getSimpleName() + " => makeJSONNote => ", e);
            Log.d(App.TAG, Proses_Photo.class.getSimpleName() + " => makeJSONNote  => " + e.toString());
        }
        return jsonObject.toString();
    }

    @NonNull
    private RequestBody createPartFromString(String description) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), description);
    }

    private void dismissDialog() {
        try {
            if (progressDialog != null)
                progressDialog.dismiss();
        } catch (Exception e) {
            Log.d(App.TAG, Proses_Photo.class.getSimpleName() + " => dismissDialog  => " + e.toString());
        }
    }
}