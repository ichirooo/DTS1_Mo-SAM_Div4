package co.id.datascrip.mo_sam_div4_dts1.process;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackCustomer;
import co.id.datascrip.mo_sam_div4_dts1.object.Customer;
import co.id.datascrip.mo_sam_div4_dts1.process.interfaces.IC_Customer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static co.id.datascrip.mo_sam_div4_dts1.Global.SISTEM;

public class Process_Customer {

    private Context context;
    private ProgressDialog progressDialog;

    public Process_Customer(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void Find(String teks, final CallbackCustomer.CBFind CB) {
        IC_Customer service = Global.CreateRetrofit(context).create(IC_Customer.class);
        service.Find(SISTEM, makeJSON(teks)).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Customer->Find", result);
                CB.onCB(RetrofitResponse.getListCustomer(result));
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Customer->Find", throwable.getMessage());
                throwable.getCause();
                progressDialog.dismiss();
            }
        });
    }

    public void Generate_Code(final CallbackCustomer.CBGenerateCode CB) {
        IC_Customer service = Global.CreateRetrofit(context).create(IC_Customer.class);
        service.Get_No(makeJSON()).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Customer->Generate_Code", result);
                CB.onCB(RetrofitResponse.getCustomerCode(result));
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Customer->Generate_Code", throwable.getMessage());
                throwable.getCause();
                progressDialog.dismiss();
            }
        });
    }

    public void Posting(final Customer customer, final CallbackCustomer.CBPosting CB) {
        IC_Customer service = Global.CreateRetrofit(context).create(IC_Customer.class);
        service.Post(makeJSON(customer)).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = RetrofitResponse.getString(context, response);
                new Function(context).writeToText("Customer->Posting", result);
                if (RetrofitResponse.isSuccess(result))
                    CB.onCB(customer);
                else CB.onCB(null);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                new Function(context).writeToText("Customer->Posting", throwable.getMessage());
                throwable.getCause();
                progressDialog.dismiss();
            }
        });
    }

    private String makeJSON(String code) {
        JSONObject json = new JSONObject();
        try {
            json.put("bex", Global.getBEX(context).getInitial());
            json.put("code", code);
        } catch (JSONException e) {
            e.toString();
        }
        return json.toString();
    }

    private String makeJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("no", Global.getBEX(context).getEmpNo());
            json.put("bex", Global.getBEX(context).getInitial());
        } catch (JSONException e) {
            e.toString();
        }
        return json.toString();
    }

    private String makeJSON(Customer customer) {
        JSONObject json = new JSONObject();
        JSONObject json_customer = new JSONObject();

        String cust_nav_code = "";
        if (!customer.getCode().toUpperCase().contains("CUST"))
            cust_nav_code = customer.getCode();

        try {
            json_customer.put("code", customer.getCode());
            json_customer.put("code_nav", cust_nav_code);
            json_customer.put("name", customer.getName());
            json_customer.put("alias", customer.getAlias());
            json_customer.put("address", customer.getAddress1());
            json_customer.put("city", customer.getCity());
            json_customer.put("contact", customer.getContact());
            json_customer.put("phone", customer.getPhone());
            json_customer.put("email", customer.getEmail());
            json_customer.put("npwp", customer.getNPWP());
            json.put("bex", Global.getBEX(context).getEmpNo());
            json.put("data", json_customer);

        } catch (JSONException e) {
            e.toString();
        }
        return json.toString();
    }
}
