package co.id.datascrip.mo_sam_div4_dts1.process;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackKegiatan;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.process.interfaces.IC_Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static co.id.datascrip.mo_sam_div4_dts1.Global.SISTEM;

public class Process_Kegiatan {

    public static final String ACTION_CANCEL = "cancel";
    public static final String ACTION_KETERANGAN = "keterangan";
    public static final String ACTION_RESULT = "result";
    public static final String ACTION_CHECK_IN = "in";
    public static final String ACTION_CHECK_OUT = "out";
    public static final String ACTION_REFRESH_TODAY = "refresh_today";
    public static final String ACTION_REFRESH_DATE = "refresh_date";

    private Context context;
    private ProgressDialog progressDialog;

    public Process_Kegiatan(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void Post(final Kegiatan kegiatan, final CallbackKegiatan.CBPost CB) {
        IC_Kegiatan service = Global.CreateRetrofit(context).create(IC_Kegiatan.class);
        service.Post(makeJSON(kegiatan)).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Kegiatan->Post", result);
                Kegiatan kegiatan_ = kegiatan;
                kegiatan_ = RetrofitResponse.getIDKegiatan(kegiatan_, result);
                new KegiatanSQLite(context).Post(kegiatan_);
                CB.onCB(kegiatan_);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Kegiatan->Post", throwable.getMessage());
                throwable.getCause();
                progressDialog.dismiss();
            }
        });
    }

    public void Action(final Kegiatan kegiatan, final String action, final CallbackKegiatan.CBAction CB) {
        IC_Kegiatan IC = Global.CreateRetrofit(context).create(IC_Kegiatan.class);
        String json = makeJSON(action, kegiatan);
        switch (action) {
            case ACTION_CANCEL:
                IC.Cancel(json).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->Action->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).Cancel(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->Action->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
            case ACTION_KETERANGAN:
                IC.Keterangan(json).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->Action->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).Keterangan(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->Action->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
            case ACTION_RESULT:
                IC.Result(json).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->Action->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).Result(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->Action->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
        }
    }

    public void CheckInCheckOut(final Kegiatan kegiatan, final String action, final CallbackKegiatan.CBCheckInCheckOut CB) {
        IC_Kegiatan IC = Global.CreateRetrofit(context).create(IC_Kegiatan.class);
        String json = makeJSON(action, kegiatan);
        switch (action) {
            case ACTION_CHECK_IN:
                IC.CheckIn(SISTEM, json).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).Update(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
            case ACTION_CHECK_OUT:
                IC.CheckOut(json).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, result);
                        if (RetrofitResponse.isSuccess(result))
                            new KegiatanSQLite(context).Update(kegiatan);
                        CB.onCB(kegiatan);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
        }
    }

    public void Refresh(final String action, final String date, final CallbackKegiatan.CBRefresh CB) {
        IC_Kegiatan IC = Global.CreateRetrofit(context).create(IC_Kegiatan.class);
        String json = makeJSON(action, date);
        switch (action) {
            case ACTION_REFRESH_TODAY:
                IC.RefreshToday(SISTEM, json).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, result);
                        if (RetrofitResponse.isSuccess(result)) {
                            ArrayList<Kegiatan> list_kegiatan = RetrofitResponse.getListKegiatan(result);
                            new KegiatanSQLite(context).setStatus(list_kegiatan);
                            CB.onCB(list_kegiatan);
                        }
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
            case ACTION_REFRESH_DATE:
                IC.RefreshDate(SISTEM, json).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String result = RetrofitResponse.getString(context, response);
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, result);
                        if (RetrofitResponse.isSuccess(result)) {
                            ArrayList<Kegiatan> list_kegiatan = RetrofitResponse.getListKegiatan(result);
                            new KegiatanSQLite(context).setStatus(list_kegiatan);
                            CB.onCB(list_kegiatan);
                        }
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        new Function(context).writeToText("Kegiatan->CheckInCheckOut->" + action, throwable.getMessage());
                        throwable.getCause();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
                break;
        }
    }

    private String makeJSON(Kegiatan kegiatan) {
        JSONObject json_kegiatan = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json_kegiatan.put("cust_code", kegiatan.getSalesHeader().getCustomer().getCode());
            if (!kegiatan.getSalesHeader().getCustomer().getCode().contains("CUST"))
                json_kegiatan.put("cust_nav", kegiatan.getSalesHeader().getCustomer().getCode());
            json_kegiatan.put("cust_name", kegiatan.getSalesHeader().getCustomer().getName());
            json_kegiatan.put("lat", kegiatan.getSalesHeader().getCustomer().getLatitude());
            json_kegiatan.put("lang", kegiatan.getSalesHeader().getCustomer().getLongitude());

            json_kegiatan.put("bex_no", kegiatan.getBEX().getEmpNo());
            json_kegiatan.put("bex_initial", kegiatan.getBEX().getInitial());

            json_kegiatan.put("id", kegiatan.getID());
            json_kegiatan.put("startDate", kegiatan.getStartDate());
            json_kegiatan.put("endDate", kegiatan.getEndDate());
            json_kegiatan.put("tipe", kegiatan.getTipe());
            json_kegiatan.put("jenis", kegiatan.getJenis());
            json_kegiatan.put("subject", kegiatan.getSubject());
            json_kegiatan.put("cancel", kegiatan.getCancel());
            json_kegiatan.put("cancel_reason", kegiatan.getReason());
            json_kegiatan.put("keterangan", kegiatan.getKeterangan());
            json_kegiatan.put("result", kegiatan.getResult());
            json_kegiatan.put("resultDate", kegiatan.getResultDate());
            json_kegiatan.put("checkIn", kegiatan.getCheckIn());

            json.put("bex", Global.getBEX(context).getInitial());
            json.put("data", json_kegiatan);

        } catch (Exception e) {
            return e.toString();
        }
        return json.toString();
    }

    private String makeJSON(String action, Kegiatan kegiatan) {
        JSONObject json = new JSONObject();
        JSONObject json_kegiatan = new JSONObject();
        try {
            switch (action) {
                case ACTION_CANCEL:
                    json_kegiatan.put("id", kegiatan.getID());
                    json_kegiatan.put("cancel", kegiatan.getCancel());
                    json_kegiatan.put("reason", kegiatan.getReason());
                    break;

                case ACTION_KETERANGAN:
                    json_kegiatan.put("id", kegiatan.getID());
                    json_kegiatan.put("keterangan", kegiatan.getKeterangan());
                    break;

                case ACTION_RESULT:
                    json_kegiatan.put("id", kegiatan.getID());
                    json_kegiatan.put("result", kegiatan.getResult());
                    break;

                case ACTION_CHECK_IN:
                    json_kegiatan.put("id", kegiatan.getID());
                    break;

                case ACTION_CHECK_OUT:
                    json_kegiatan.put("id", kegiatan.getID());
                    break;
            }
            json.put("bex", Global.getBEX(context).getInitial());
            json.put("data", json_kegiatan);
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }
        return json.toString();
    }

    private String makeJSON(String action, String date) {
        JSONObject json = new JSONObject();
        JSONObject json_kegiatan = new JSONObject();
        try {
            switch (action) {
                case ACTION_REFRESH_TODAY:
                    break;

                case ACTION_REFRESH_DATE:
                    json_kegiatan.put("date", date);
                    break;
            }
            json.put("bex", Global.getBEX(context).getInitial());
            json.put("data", json_kegiatan);
        } catch (Exception e) {
            // TODO: handle exception
            return e.toString();
        }
        return json.toString();
    }
}
