package com.salamander.mo_sam_div4_dts1.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.font.RobotoEditText;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Customer;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_logger.LoggingExceptionHandler;
import com.salamander.salamander_network.RetroStatus;

public class Input_Customer_Activity extends AppCompatActivity {

    private Context context;
    private RobotoEditText tx_customer_code, tx_customer_name, tx_customer_alias, tx_customer_address_1, tx_customer_address_2, tx_customer_contact, tx_customer_phone, tx_customer_email, tx_city, tx_customer_npwp;
    private Button bt_ok, bt_cancel;
    private Customer customer;
    private LinearLayout ll_button_ok_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new LoggingExceptionHandler(this));
        setContentView(R.layout.activity_input_customer);
        context = this;
        customer = getIntent().getParcelableExtra("customer");
        if (customer == null)
            customer = new Customer();
        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Input New Customer");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean Validate() {
        if (Utils.isEmpty(tx_customer_name)) {
            tx_customer_name.requestFocus();
            Toast.makeText(this, "Customer Name harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Utils.isEmpty(tx_customer_address_1)) {
            tx_customer_address_1.requestFocus();
            Toast.makeText(this, "Address harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        }/* else if (tx_sp_city.getText().toString().toLowerCase().contains("pilih")) {
            tx_sp_city.requestFocus();
            Toast.makeText(this, "Pilih Kota.", Toast.LENGTH_SHORT).show();
            return false;
        } */ else if (Utils.isEmpty(tx_city)) {
            tx_city.requestFocus();
            Toast.makeText(this, "City harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Utils.isEmpty(tx_customer_contact)) {
            tx_customer_contact.requestFocus();
            Toast.makeText(this, "Contact Person harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Utils.isEmpty(tx_customer_phone)) {
            tx_customer_phone.requestFocus();
            Toast.makeText(this, "No Telepon harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        if (Utils.isEmpty(customer.getCode()))
            new Proses_Customer(context).generateCode(new Callbacks.OnCBGenerateCodeCustomer() {
                @Override
                public void onCB(RetroStatus retroStatus, String customer_code) {
                    if (retroStatus.isSuccess()) {
                        customer.setCode(customer_code);
                        customer.setCodeNAV(customer_code);
                        tx_customer_code.setText(customer_code);
                        tx_customer_name.setText(getIntent().getStringExtra(Customer.CUSTOMER_NAME));
                    } else DialogUtils.showErrorMessage(context, retroStatus.getMessage(), true);
                }
            });
        else {
            tx_customer_code.setText(customer.getCode());
            tx_customer_name.setText(customer.getName());
            tx_customer_alias.setText(customer.getAlias());
            tx_customer_address_1.setText(customer.getAddress1());
            tx_customer_address_2.setText(customer.getAddress2());
            tx_city.setText(customer.getCity());
            tx_customer_contact.setText(customer.getContactPerson());
            tx_customer_phone.setText(customer.getPhone());
            tx_customer_email.setText(customer.getEmail());
            tx_customer_npwp.setText(customer.getNPWP());
        }
    }

    private void initView() {
        tx_customer_code = findViewById(R.id.tx_customer_code);
        tx_customer_name = findViewById(R.id.tx_customer_name);
        tx_customer_alias = findViewById(R.id.tx_customer_alias);
        tx_customer_address_1 = findViewById(R.id.tx_customer_address_1);
        tx_customer_address_2 = findViewById(R.id.tx_customer_address_2);
        tx_city = findViewById(R.id.tx_city);
        tx_customer_contact = findViewById(R.id.tx_customer_contact);
        tx_customer_phone = findViewById(R.id.tx_customer_phone);
        tx_customer_email = findViewById(R.id.tx_customer_email);
        tx_customer_npwp = findViewById(R.id.tx_customer_npwp);
        ll_button_ok_cancel = findViewById(R.id.ll_button_ok_cancel);
        bt_ok = findViewById(R.id.bt_ok);
        bt_cancel = findViewById(R.id.bt_cancel);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate()) {
                    customer.setCode(tx_customer_code.getText().toString());
                    customer.setName(tx_customer_name.getText().toString());
                    customer.setAddress1(tx_customer_address_1.getText().toString());
                    customer.setAddress2(tx_customer_address_2.getText().toString());
                    customer.setCity(tx_city.getText().toString());
                    customer.setContactPerson(tx_customer_contact.getText().toString());
                    customer.setPhone(tx_customer_phone.getText().toString());
                    customer.setEmail(tx_customer_email.getText().toString());
                    customer.setNPWP(tx_customer_npwp.getText().toString());
                    customer.setLatitude(0);
                    customer.setLongitude(0);

                    new Proses_Customer(context).post(customer, new Callbacks.OnCB() {
                        @Override
                        public void onCB(RetroStatus retroStatus) {
                            if (retroStatus.isSuccess()) {
                                Intent intent = new Intent();
                                intent.putExtra("customer", customer);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else
                                DialogUtils.showErrorMessage(context, retroStatus.getMessage(), false);
                        }
                    });
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        setData();
    }

    private void setEnabled(boolean enabled) {
        tx_customer_code.setEnabled(enabled);
        tx_customer_name.setEnabled(enabled);
        tx_customer_alias.setEnabled(enabled);
        tx_customer_address_1.setEnabled(enabled);
        tx_customer_address_2.setEnabled(enabled);
        tx_city.setEnabled(enabled);
        tx_customer_contact.setEnabled(enabled);
        tx_customer_phone.setEnabled(enabled);
        tx_customer_email.setEnabled(enabled);
        tx_customer_npwp.setEnabled(enabled);
        if (enabled)
            ll_button_ok_cancel.setVisibility(View.VISIBLE);
        else ll_button_ok_cancel.setVisibility(View.GONE);
    }
}
