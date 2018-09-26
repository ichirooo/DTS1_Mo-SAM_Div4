package com.salamander.mo_sam_div4_dts1.proses;

import android.app.ProgressDialog;
import android.content.Context;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.proses.interfaces.IC_Customer;
import com.salamander.mo_sam_div4_dts1.sqlite.CustomerSQLite;
import com.salamander.salamander_network.RetroData;
import com.salamander.salamander_network.RetroResp;

import okhttp3.ResponseBody;

public class Proses_Customer {

    private Context context;
    private ProgressDialog progressDialog;
    private IC_Customer IC;

    public Proses_Customer(Context context) {
        this.context = context;
        IC = App.createRetrofit(context).create(IC_Customer.class);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void find(final String searchText, final Callbacks.onCBCustomers CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                find(searchText, CB);
            }
        })) {
            IC.find(App.getUser(context).getEmpNo(), searchText).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
                @Override
                public void onCall(RetroData retroData) {
                    super.onCall(retroData);
                    CB.onCB(retroData.getRetroStatus(), RetrofitResponse.getListCustomer(context, retroData.getResult()));
                    RetroResp.dismissDialog(progressDialog);
                }
            });
        } else {
            RetroResp.dismissDialog(progressDialog);
        }
    }

    public void generateCode(final Callbacks.OnCBGenerateCodeCustomer CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                generateCode(CB);
            }
        })) {
            IC.generateCode(App.getUser(context).getEmpNo(), App.getUser(context).getInitial()).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
                @Override
                public void onCall(RetroData retroData) {
                    super.onCall(retroData);
                    CB.onCB(retroData.getRetroStatus(), RetrofitResponse.getCustomerCode(retroData.getResult()));
                    RetroResp.dismissDialog(progressDialog);
                }
            });
        } else {
            RetroResp.dismissDialog(progressDialog);
        }
    }

    public void post(final Customer customer, final Callbacks.OnCB CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                post(customer, CB);
            }
        })) {
            if (!customer.getCode().toUpperCase().contains("CUST"))
                customer.setCodeNAV(customer.getCode());
            IC.post(App.getUser(context).getEmpNo(), customer.getAsJSON().toString()).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context, customer.getAsJSON().toString()) {
                @Override
                public void onCall(RetroData retroData) {
                    super.onCall(retroData);
                    if (retroData.isSuccess())
                        new CustomerSQLite(context).Post(customer);
                    CB.onCB(retroData.getRetroStatus());
                    RetroResp.dismissDialog(progressDialog);
                }
            });
        } else {
            RetroResp.dismissDialog(progressDialog);
        }
    }

    public void getAll(final Callbacks.onCBCustomers CB) {
        if (App.isConnectedToInternet(context, new App.OnTryAgain() {
            @Override
            public void onTryAgain() {
                getAll(CB);
            }
        })) {
            IC.getAll(App.getUser(context).getEmpNo(), App.getUser(context).getInitial()).enqueue(new RetroResp.SuccessCallback<ResponseBody>(context) {
                @Override
                public void onCall(RetroData retroData) {
                    super.onCall(retroData);
                    CB.onCB(retroData.getRetroStatus(), RetrofitResponse.getListCustomer(context, retroData.getResult()));
                    RetroResp.dismissDialog(progressDialog);
                }
            });
        } else {
            RetroResp.dismissDialog(progressDialog);
        }
    }
}
