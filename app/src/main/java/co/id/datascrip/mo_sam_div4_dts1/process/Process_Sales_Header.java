package co.id.datascrip.mo_sam_div4_dts1.process;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.Const;
import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackSalesHeader;
import co.id.datascrip.mo_sam_div4_dts1.object.Item;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.object.SalesHeader;
import co.id.datascrip.mo_sam_div4_dts1.process.interfaces.IC_Order;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.SalesHeaderSQLite;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static co.id.datascrip.mo_sam_div4_dts1.Global.SISTEM;

public class Process_Sales_Header {

    private Context context;
    private ProgressDialog progressDialog;

    public Process_Sales_Header(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void Generate_No_Order(int id_kegiatan, final CallbackSalesHeader.CBNoOrder CB) {
        IC_Order service = Global.CreateRetrofit(context).create(IC_Order.class);
        service.GetNo(SISTEM, makeJSON(id_kegiatan)).enqueue(new Callback<ResponseBody>() {
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
        IC_Order service = Global.CreateRetrofit(context).create(IC_Order.class);
        service.FindItem(SISTEM, makeJSON(currency, code)).enqueue(new Callback<ResponseBody>() {
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
        new KegiatanSQLite(context).Post(kegiatan);

        kegiatan.getSalesHeader().setOrderNo(no_order);
        IC_Order service = Global.CreateRetrofit(context).create(IC_Order.class);
        service.Post(SISTEM, makeJSON(kegiatan)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("SalesHeader->Posting", result);
                Kegiatan kegiatan_new = RetrofitResponse.PostSalesHeader(kegiatan, result, no_order);
                if (kegiatan_new.getSalesHeader().getHeaderID() != 0 && kegiatan_new.getSalesHeader().getHeaderID() < Const.DEFAULT_HEADER_ID)
                    new KegiatanSQLite(context).Post(kegiatan_new);
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
        IC_Order service = Global.CreateRetrofit(context).create(IC_Order.class);
        service.CheckStatus(makeJSON(id_header)).enqueue(new Callback<ResponseBody>() {
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
        IC_Order service = Global.CreateRetrofit(context).create(IC_Order.class);
        service.GetPrice(SISTEM, makeJSON("IDR", code)).enqueue(new Callback<ResponseBody>() {
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

    private String makeJSON(Kegiatan kegiatan) {
        JSONObject json_all = new JSONObject();
        JSONObject json = new JSONObject();
        JSONObject json_header = new JSONObject();
        JSONObject json_customer = new JSONObject();
        JSONObject json_bex = new JSONObject();
        JSONArray json_line = new JSONArray();

        try {
            SalesHeader salesHeader = kegiatan.getSalesHeader();

            if (salesHeader.getHeaderID() >= Const.DEFAULT_HEADER_ID || salesHeader.getHeaderID() == 0)
                json_all.put("action", "insert");
            else
                json_all.put("action", "update");

            if (salesHeader.getHeaderID() >= Const.DEFAULT_HEADER_ID)
                json_header.put("id", 0);
            else json_header.put("id", salesHeader.getHeaderID());
            json_header.put("id_kegiatan", kegiatan.getID());
            json_header.put("andro_so", salesHeader.getOrderNo());
            json_header.put("syarat", salesHeader.getSyarat());
            json_header.put("tempo", salesHeader.getTempo());
            json_header.put("currency", salesHeader.getCurrency());
            json_header.put("promised_date", salesHeader.getPromisedDate());
            json_header.put("subtotal", salesHeader.getSubtotal());
            json_header.put("potongan", salesHeader.getPotongan());
            json_header.put("dpp", salesHeader.getDPP());
            json_header.put("ppn", salesHeader.getPPnDPP());
            json_header.put("total", salesHeader.getTotal());
            json_header.put("notes", salesHeader.getNotes());

            json_customer.put("code", salesHeader.getCustomer().getCode());
            json_customer.put("name", salesHeader.getCustomer().getName());
            json_customer.put("address1", salesHeader.getCustomer().getAddress1());
            json_customer.put("address2", salesHeader.getCustomer().getAddress2());
            json_customer.put("city", salesHeader.getCustomer().getCity());
            json_customer.put("contact", salesHeader.getCustomer().getContact());
            json_customer.put("npwp", salesHeader.getCustomer().getNPWP());
            json_customer.put("phone", salesHeader.getCustomer().getPhone());
            json_customer.put("lat", salesHeader.getCustomer().getLatitude());
            json_customer.put("lang", salesHeader.getCustomer().getLongitude());

            json_bex.put("emp_no", kegiatan.getBEX().getEmpNo());
            json_bex.put("initial", kegiatan.getBEX().getInitial());

            ArrayList<Item> salesLine = salesHeader.getSalesLine();
            for (int i = 0; i < salesLine.size(); i++) {
                JSONObject json_item = new JSONObject();
                json_item.put("lineID", salesLine.get(i).getID());
                json_item.put("code", salesLine.get(i).getCode());
                json_item.put("price", salesLine.get(i).getPrice());
                json_item.put("discount", salesLine.get(i).getDiscount());
                json_item.put("disc_pct", salesLine.get(i).getDiscPct());
                json_item.put("qty", salesLine.get(i).getQuantity());
                json_line.put(json_item);
            }
            json.put("bex", json_bex);
            json.put("header", json_header);
            json.put("customer", json_customer);
            json.put("line", json_line);
            json_all.put("bex", Global.getBEX(context).getInitial());
            json_all.put("data", json);
        } catch (JSONException e) {
            return e.toString();
        }
        return json_all.toString();
    }

    private String makeJSON(String currency, String code) {
        JSONObject json = new JSONObject();
        JSONObject json_item = new JSONObject();
        try {
            json_item.put("currency", currency);
            json_item.put("code", code);
            json.put("bex", Global.getBEX(context).getInitial());
            json.put("data", json_item);
        } catch (JSONException e) {
            new Function(context).writeToText("Process_Sales_Header->makeJSON", e.toString());
        }
        return json.toString();
    }

    private String makeJSON(int id_kegiatan) {
        JSONObject json = new JSONObject();
        try {
            json.put("idk", id_kegiatan);
            json.put("bex", Global.getBEX(context).getInitial());
        } catch (JSONException e) {
            new Function(context).writeToText("Process_Sales_Header->makeJSON", e.toString());
        }
        return json.toString();
    }
}
