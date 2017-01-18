package co.id.datascrip.mo_sam_div4_dts1.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackCustomer;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackCustomer.CBGenerateCode;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoButton;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoEditText;
import co.id.datascrip.mo_sam_div4_dts1.object.Customer;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Customer;

public class Input_Customer_Activity extends AppCompatActivity {

    private Context context;
    private RobotoEditText txCode, txName, txAlias, txAddress, txCity, txContact,
            txPhone, txEmail, txNPWP;
    private RobotoButton btOK, btCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_customer);
        context = this;
        initToolbar();

        txCode = (RobotoEditText) findViewById(R.id.ica_cust_code);
        txName = (RobotoEditText) findViewById(R.id.ica_cust_name);
        txAlias = (RobotoEditText) findViewById(R.id.ica_cust_alias);
        txAddress = (RobotoEditText) findViewById(R.id.ica_cust_address);
        txCity = (RobotoEditText) findViewById(R.id.ica_cust_city);
        txContact = (RobotoEditText) findViewById(R.id.ica_cust_contact);
        txPhone = (RobotoEditText) findViewById(R.id.ica_cust_phone);
        txEmail = (RobotoEditText) findViewById(R.id.ica_cust_email);
        txNPWP = (RobotoEditText) findViewById(R.id.ica_cust_npwp);
        btOK = (RobotoButton) findViewById(R.id.ica_btOK);
        btCancel = (RobotoButton) findViewById(R.id.ica_btCancel);

        setData();

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate()) {
                    Customer customer = new Customer();
                    customer.setCode(txCode.getText().toString());
                    customer.setName(txName.getText().toString());
                    customer.setAlias(txAlias.getText().toString());
                    customer.setAddress1(txAddress.getText().toString());
                    customer.setCity(txCity.getText().toString());
                    customer.setContact(txContact.getText().toString());
                    customer.setPhone(txPhone.getText().toString());
                    customer.setEmail(txEmail.getText().toString());
                    customer.setNPWP(txNPWP.getText().toString());
                    customer.setLatitude(0);
                    customer.setLongitude(0);

                    new Process_Customer(context).Posting(customer, new CallbackCustomer.CBPosting() {
                        @Override
                        public void onCB(Customer customer) {
                            if (customer != null) {
                                Intent intent = new Intent();
                                intent.putExtra("customer", customer);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Input New Customer");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean Validate() {
        if (!isValid(txName)) {
            txName.requestFocus();
            Toast.makeText(this, "Customer Name harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValid(txAddress)) {
            txAddress.requestFocus();
            Toast.makeText(this, "Alamat harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValid(txCity)) {
            txCity.requestFocus();
            Toast.makeText(this, "Kota harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValid(txContact)) {
            txContact.requestFocus();
            Toast.makeText(this, "Contact Person harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValid(txPhone)) {
            txPhone.requestFocus();
            Toast.makeText(this, "No Telepon harus diisi.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValid(RobotoEditText e) {
        if (e.getText() != null) {
            if (!e.getText().toString().trim().isEmpty())
                return true;
        }
        return false;
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
        new Process_Customer(this).Generate_Code(new CBGenerateCode() {
            @Override
            public void onCB(String customer_code) {
                if (customer_code != null)
                    txCode.setText(customer_code);
                txName.setText(getIntent().getStringExtra("cust_name"));
            }
        });
    }
}
