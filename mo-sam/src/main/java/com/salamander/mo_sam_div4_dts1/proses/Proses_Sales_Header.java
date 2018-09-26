package com.salamander.mo_sam_div4_dts1.proses;

import android.app.ProgressDialog;
import android.content.Context;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Function;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.object.SalesHeader;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Order;
import com.salamander.salamander_network.JSON;
import com.salamander.salamander_network.RetroData;
import com.salamander.salamander_network.RetroResp;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;

public class Proses_Sales_Header {

    private Context context;
    private IC_Order IC;
    private ProgressDialog progressDialog;

    public Proses_Sales_Header(Context context) {
        this.context = context;
        IC = App.createRetrofit(context).create(IC_Order.class);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public Proses_Sales_Header(Context context, String loadingMessage) {
        this.context = context;
        IC = App.createRetrofit(context).create(IC_Order.class);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(loadingMessage);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void generateNoOrder(int id_order, final Callbacks.OnCBGenerateNoOrder CB) {
        IC.generateNoOrder(App.getUser(context).getEmpNo(), makeJSON(id_order)).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context, makeJSON(id_order)) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                CB.onCB(retroData.getRetroStatus(), RetrofitResponse.getNoOrder(retroData.getResult()));
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void findItem(String code, final Callbacks.OnCBFindItem CB) {
        IC.findItem(App.getUser(context).getEmpNo(), code).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context, makeJSON(code)) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                CB.onCB(retroData.getRetroStatus(), RetrofitResponse.getListItem(retroData.getResult()));
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void checkOrderStatus(int id_order, final Callbacks.OnCBCheckOrderStatus CB) {
        IC.checkOrderStatus(App.getUser(context).getEmpNo(), id_order).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                CB.onCB(retroData.getRetroStatus(), JSON.getInt(JSON.toJSONObject(retroData.getResult()), SalesHeader.SALES_HEADER_STATUS));
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void getPrice(String item_code, final Callbacks.OnCBGetPrice CB) {
        IC.getPrice(App.getUser(context).getEmpNo(), item_code).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                CB.onCB(retroData.getRetroStatus(), JSON.getDouble(JSON.toJSONObject(retroData.getResult()), Item.ITEM_PRICE));
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }

    public void post(final SalesHeader salesHeader, final Callbacks.OnCB CB) {
        IC.Post(App.getUser(context).getEmpNo(), salesHeader.getAsJSON().toString()).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
            @Override
            public void onCall(RetroData retroData) {
                super.onCall(retroData);
                if (retroData.isSuccess()) {
                    RetrofitResponse.getOrderData(context, retroData.getResult(), salesHeader);
                }
                CB.onCB(retroData.getRetroStatus());
                RetroResp.dismissDialog(progressDialog);
            }
        });
    }
/*
    public void Generate_No_Order(int id_kegiatan, final CallbackSalesHeader.CBNoOrder CB) {
        service.GetNo(SISTEM, makeJSON(id_kegiatan)).enqueue(new Callbacks<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("SalesHeader->Generate_No_Order", result);
                if (RetrofitResponse.isSuccess(result))
                    CB.onCB(RetrofitResponse.getNoOrder(result));
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("SalesHeader->Generate_No_Order", throwable.getMessage());
                if (progressDialog != null)
                    progressDialog.dismiss();
                throwable.getCause();
            }
        });
    }

    public void Find_Item(String currency, String code, final CallbackSalesHeader.CBFindItem CB) {
        IC_Order service = App.createRetrofit(context).create(IC_Order.class);
        service.FindItem(SISTEM, makeJSON(currency, code)).enqueue(new Callbacks<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("SalesHeader->Find_Item", result);
                CB.onCB(RetrofitResponse.getListItem(result));
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("SalesHeader->Find_Item", throwable.getMessage());
                throwable.getCause();
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

    public void Posting(final Kegiatan kegiatan, final CallbackSalesHeader.CBPosting CB) {

        final String no_order = kegiatan.getSalesHeader().getOrderNo();
        kegiatan.getSalesHeader().setOrderNo(new SalesHeaderSQLite(context).getTempOrderNo());
        new KegiatanSQLite(context).post(kegiatan);

        kegiatan.getSalesHeader().setOrderNo(no_order);
        IC_Order service = App.createRetrofit(context).create(IC_Order.class);
        service.post(SISTEM, makeJSON(kegiatan)).enqueue(new Callbacks<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("SalesHeader->Posting", result);
                Kegiatan kegiatan_new = RetrofitResponse.PostSalesHeader(kegiatan, result, no_order);
                if (kegiatan_new.getSalesHeader().getIDServer() != 0 && kegiatan_new.getSalesHeader().getIDServer() < Const.DEFAULT_HEADER_ID)
                    new KegiatanSQLite(context).post(kegiatan_new);
                else {
                    kegiatan_new = null;
                }
                CB.onCB(kegiatan_new);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("SalesHeader->Posting", throwable.getMessage());
                if (progressDialog != null)
                    progressDialog.dismiss();
                throwable.getCause();
            }
        });
    }

    public void CheckStatus(int id_header, final CallbackSalesHeader.CBCheckStatus CB) {
        IC_Order service = App.createRetrofit(context).create(IC_Order.class);
        service.CheckStatus(makeJSON(id_header)).enqueue(new Callbacks<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("SalesHeader->CheckStatus", result);
                if (RetrofitResponse.isSuccess(result))
                    CB.onCB(RetrofitResponse.getStatusOrder(result));
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("SalesHeader->CheckStatus", throwable.getMessage());
                throwable.getCause();
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

    public void GetPrice(String code, final CallbackSalesHeader.CBGetPrice CB) {
        IC_Order service = App.createRetrofit(context).create(IC_Order.class);
        service.GetPrice(SISTEM, makeJSON("IDR", code)).enqueue(new Callbacks<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("SalesHeader->GetPrice", result);
                CB.onCB(result != null, RetrofitResponse.getPrice(result));
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("SalesHeader->GetPrice", throwable.getMessage());
                throwable.getCause();
                CB.onCB(false, 0);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }
    */

    private String makeJSON(String code) {
        JSONObject json = new JSONObject();
        try {
            json.put(Item.ITEM_CODE, code);
        } catch (JSONException e) {
            new Function(context).writeToText("Proses_Sales_Header->makeJSON", e.toString());
        }
        return json.toString();
    }

    private String makeJSON(int id_order) {
        JSONObject json = new JSONObject();
        try {
            json.put(SalesHeader.SALES_HEADER_ID_SERVER, id_order);
            json.put(User.BEX_INITIAL, App.getUser(context).getInitial());
        } catch (JSONException e) {
            new Function(context).writeToText("Proses_Sales_Header->makeJSON", e.toString());
        }
        return json.toString();
    }
}
