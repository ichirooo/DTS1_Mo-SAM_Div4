package co.id.datascrip.mo_sam_div4_dts1.process;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackPhoto;
import co.id.datascrip.mo_sam_div4_dts1.object.Photo;
import co.id.datascrip.mo_sam_div4_dts1.process.interfaces.IC_Photo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Process_Photo {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private ProgressDialog progressDialog;
    private Context context;

    public Process_Photo(Context context) {
        this.context = context;
        this.progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public void Upload(ArrayList<Photo> list_photo, final CallbackPhoto.CBUploadPhoto CB) {
        IC_Photo service = Global.CreateRetrofit(context).create(IC_Photo.class);
        ArrayList<MultipartBody.Part> list_part = new ArrayList<>();

        for (int i = 0; i < list_photo.size(); i++) {
            File file = new File(list_photo.get(i).getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo" + String.valueOf(i + 1), file.getName(), requestFile);
            list_part.add(body);
        }

        Call<ResponseBody> call = service.Upload(createPartFromString(String.valueOf(list_photo.size())),
                createPartFromString(makeJSON(list_photo)),
                list_part);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                if (RetrofitResponse.isSuccess(result)) {
                    CB.onCB(true);
                    if (progressDialog != null)
                        progressDialog.dismiss();
                } else {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    showErrorMessage(result, false);
                    CB.onCB(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Upload", throwable.getMessage());
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                CB.onCB(false);
            }
        });

    }

    private void showErrorMessage(String json, final boolean finish) {
        if (json != null)
            new AlertDialog.Builder(context)
                    .setMessage(RetrofitResponse.getErrorMessage(json))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (finish)
                                ((Activity) context).finish();
                        }
                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (finish)
                                ((Activity) context).finish();
                        }
                    })
                    .create()
                    .show();
    }

    private String makeJSON(ArrayList<Photo> photos) {
        JSONObject json = new JSONObject();
        JSONArray ja_photo = new JSONArray();

        try {
            for (int i = 0; i < photos.size(); i++) {
                JSONObject json_photo = new JSONObject();
                json_photo.put("id", photos.get(i).getID());
                json_photo.put("info", photos.get(i).getInfo());
                json_photo.put("name", new File(photos.get(i).getPath()).getName());
                ja_photo.put(json_photo);
            }
            json.put("idk", photos.get(0).getIdKegiatan());
            json.put("bex", Global.getBEX(context).getInitial());
            json.put("bex_no", Global.getBEX(context).getEmpNo());
            json.put("data", ja_photo);
        } catch (JSONException e) {
            new Function(context).writeToText("Upload->makeJSON", e.toString());
        }

        return json.toString();
    }

    @NonNull
    private RequestBody createPartFromString(String description) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), description);
    }

}
