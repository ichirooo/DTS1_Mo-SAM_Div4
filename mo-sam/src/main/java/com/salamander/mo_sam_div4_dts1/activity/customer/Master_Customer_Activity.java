package com.salamander.mo_sam_div4_dts1.activity.customer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.ToolbarActivity;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_List_Customer;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Customer;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.CustomerSQLite;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_network.RetroStatus;

import java.util.ArrayList;

public class Master_Customer_Activity extends ToolbarActivity {

    public static final int INTENT_EDIT_CUSTOMER = 10002;

    private Activity activity;
    private RecyclerView rv_list_customer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isRefreshing;

    private Adapter_List_Customer adapterListCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_customer);
        activity = this;
        initToolbar(R.string.title_activity_master_customer);
        initView();
    }

    private void initView() {
        rv_list_customer = findViewById(R.id.rv_list_customer);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(Color.CYAN, Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefreshing) {
                    swipeRefreshLayout.setRefreshing(true);
                    refresh();
                }
            }
        });
        adapterListCustomer = new Adapter_List_Customer(this);

        rv_list_customer.setAdapter(adapterListCustomer);
        rv_list_customer.setNestedScrollingEnabled(false);
        rv_list_customer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void refresh() {
        new Proses_Customer(activity).getAll(new Callbacks.onCBCustomers() {
            @Override
            public void onCB(RetroStatus status, ArrayList<Customer> list_customer) {
                if (status.isSuccess()) {
                    CustomerSQLite customerSQLite = new CustomerSQLite(activity);
                    for (Customer customer : list_customer) {
                        customer.setBEX(App.getUser(activity).getEmpNo());
                        customerSQLite.Post(customer);
                    }
                    adapterListCustomer = new Adapter_List_Customer(activity);
                    rv_list_customer.setAdapter(adapterListCustomer);
                    rv_list_customer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } else DialogUtils.showErrorNetwork(activity, null, status.getMessage(), false);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_EDIT_CUSTOMER && resultCode == Activity.RESULT_OK) {
            adapterListCustomer = new Adapter_List_Customer(activity);
            rv_list_customer.setAdapter(adapterListCustomer);
            rv_list_customer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_master_customer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sync_customer:
                refresh();
                return true;
            case R.id.action_new_customer:
                startActivityForResult(new Intent(this, Input_Customer_Activity.class), INTENT_EDIT_CUSTOMER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
